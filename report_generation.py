import sys
import json
import pprint
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
    doc_path = sys.argv[3] + "/tmp/json/" + sys.argv[1] + ".json"
    text = ""
    text_file = open(doc_path, "r")
    for line in text_file:
        text = text + line
    text_file.close()
    json_tmp = json.loads(text)
    return json_tmp


def json_recovery():
    i_tmp = 1
    json_tmp = ""
    json_file = open(sys.argv[2], 'r')
    for line in json_file:
        if i_tmp == 1:
            json_tmp = '{"data":[' + line + ","
        elif i_tmp == len(json_input['sentences']):
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


def frame_per_sentence():
    nb_frames = 0
    nb_sentence = 0
    i = 0
    while i < len(data):
        if data[i]['frames']:
            nb_frames += len(data[i]['frames'])
            nb_sentence += 1
        i += 1
    return nb_frames / nb_sentence


def md_report():
    md_report = open('markdown_report_' + sys.argv[1] + '.md', 'w+')

    md_report.write('# Pipeline Report \n')

    md_report.write('## Primary Corpus \n')

    md_report.write('*number of tokens* (words) : ' + str(nb_tokens) + '\n')

    md_report.write('*number of sentences* : ' + str(len(json_input['sentences'])) + '\n')

    md_report.write('*average of tokens per sentence* : ' + str(int(nb_tokens / len(json_input['sentences']))) + '\n')

    md_report.write('## Analysis Report \n')

    md_report.write('*number of sentences with at least one recognized frame* : ' + str(nb_frames_rec) + '\n')

    md_report.write('*among them, average of frames per sentence* : ' + str(frame_per_sentence()))


if __name__ == '__main__':

    json_input = text_recovery()

    json_text = json_recovery()

    data_tmp = json.loads(json_text)

    data = data_tmp['data']

    frames_rec_list = []

    nb_frames_rec, nb_tokens = stats()

    md_report()

    try:

        from pylatex import Document, Section, LongTable, MiniPage, LargeText, Package

        pp = pprint.PrettyPrinter(indent=2, depth=10, compact=True, width=200)

        geometry_options = {"top": "3cm", "left": "3cm", "right": "3cm", "bottom": "3cm", "marginparwidth": "1.75cm"}
        doc = Document(geometry_options=geometry_options)

        doc.packages.append(Package('listings'))

        with doc.create(MiniPage(align='c')):
            doc.append(LargeText(bold("Pipeline Report")))

        with doc.create(Section('Primary corpus')):
            doc.append('number of tokens (words) : ' + str(nb_tokens))
            doc.append('\nnumber of sentences : ' + str(len(json_input['sentences'])))
            doc.append('\naverage of tokens per sentence : ' + str(int(nb_tokens / len(json_input['sentences']))))
            doc.append("\nnumber of sentences with at least one recognized frame : " + str(nb_frames_rec))
            doc.append("\namong them, average of frames per sentence : " + str(frame_per_sentence()))

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
                sentence = ""
                for i in range(len(json_input['sentences'])):
                    for j in range(len(json_input['sentences'][i]['tokens'])):
                        sentence += str(json_input['sentences'][i]['tokens'][j]['word']) + ' '
                    row = [str(i + 1), sentence, pp.pformat(data[i])]
                    if data[i] not in frames_rec_list:
                        data_table.add_row(row, color="lightgray!80")
                    else:
                        data_table.add_row(row)
                    data_table.add_hline()
                    sentence = ""

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

        doc.generate_pdf('report_' + sys.argv[1], clean_tex=False)

    except UnicodeDecodeError:
        print("An error occurred, UnicodeDecodeError")
    except FileNotFoundError:
        print("An error occurred, probably missing Latex interpreter (FileNotFoundError)")
    except:
        print("Unexpected error : ", sys.exc_info()[0])
