import sys
import json
import pprint
from nltk.tokenize import sent_tokenize
from pylatex.utils import bold


def sort_frames(x):
    ite = 0
    if len(frames_rec_list) == 0:
        frames_rec_list.append(x)
    else:
        while ite < len(frames_rec_list) and x['frames'][0]['annotationSets'][0]['score'] < \
                frames_rec_list[ite]['frames'][0]['annotationSets'][0]['score']:
            ite += 1
        frames_rec_list.insert(ite, x)


def text_recovery():
    text = ""
    text_file = open("data/" + sys.argv[1], "r")
    for line in text_file:
        text = text + line
    text_file.close()
    return sent_tokenize(text)


def json_recovery():
    i_tmp = 1
    json_tmp = ""
    json_file = open(sys.argv[2], 'r')
    for line in json_file:
        if i_tmp == 1:
            json_tmp = '{"data":[' + line + ","
        elif i_tmp == len(sent_tokenize_list):
            json_tmp = json_tmp + line + "]}"
        else:
            json_tmp = json_tmp + line + ","
        i_tmp += 1
    json_file.close()
    return json_tmp


def stats():
    nb_frames_rec_tmp = 0
    nb_tokens_tmp = 0
    i = 0
    while i < len(data):
        if data[i]['frames']:
            sort_frames(data[i])
            nb_frames_rec_tmp += 1
        nb_tokens_tmp += len(data[i]['tokens'])
        i += 1
    return nb_frames_rec_tmp, nb_tokens_tmp


if __name__ == '__main__':

    try:

        from pylatex import Document, Section, LongTable, MiniPage, LargeText, Package

        pp = pprint.PrettyPrinter(indent=2, depth=10, compact=True, width=200)

        sent_tokenize_list = text_recovery()

        json_text = json_recovery()

        data_tmp = json.loads(json_text)

        data = data_tmp['data']

        frames_rec_list = []

        nb_frames_rec, nb_tokens = stats()

        geometry_options = {"top": "3cm", "left": "3cm", "right": "3cm", "bottom": "3cm", "marginparwidth": "1.75cm"}
        doc = Document(geometry_options=geometry_options)

        doc.packages.append(Package('listings'))

        with doc.create(MiniPage(align='c')):
            doc.append(LargeText(bold("Pipeline Report")))

        with doc.create(Section('Primary corpus')):
            doc.append('number of tokens (words) : ' + str(nb_tokens))
            doc.append("\nnumber of frames : " + str(nb_frames_rec))
            doc.append('\nnumber of sentences : ' + str(len(sent_tokenize_list)))
            doc.append('\naverage of tokens per sentence : ' + str(int(nb_tokens / len(sent_tokenize_list))))

        with doc.create(Section('Data alignment')):
            with doc.create(LongTable("|l |p{5.5cm} |p{9cm}|")) as data_table:
                data_table.add_hline()
                data_table.add_row(["ID", "sentence", "associate analysis"])
                data_table.add_hline()
                data_table.end_table_header()
                data_table.add_hline()
                data_table.end_table_footer()

                data_table.end_table_last_footer()
                row = ["", "", ""]
                for i in range(len(sent_tokenize_list)):
                    row = [str(i + 1), sent_tokenize_list[i], pp.pformat(data[i])]
                    if data[i] in frames_rec_list:
                        data_table.add_row(row, color="lightgray")
                    else:
                        data_table.add_row(row)
                    data_table.add_hline()

        with doc.create(Section('Frames sorted by score')):
            with doc.create(LongTable("|p{15.5cm}|")) as data_table_tri:
                data_table_tri.add_hline()
                data_table_tri.add_row(["frame analysis"])
                data_table_tri.add_hline()
                data_table_tri.end_table_header()
                data_table_tri.add_hline()
                data_table_tri.end_table_footer()

                data_table_tri.end_table_last_footer()
                row = [""]
                for i in range(len(frames_rec_list)):
                    row = [pp.pformat(frames_rec_list[i])]
                    data_table_tri.add_row(row)
                    data_table_tri.add_hline()

        doc.generate_pdf('full', clean_tex=False)

    except:
        print("An error occurred, probably a Latex compiler missed")
