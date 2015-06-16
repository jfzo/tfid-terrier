f = open("biterm_degrees_bydoc.csv")
OUTDIR = "./biterm-nets"
fout = -1
for l in f:
    if l[0] == '>':
        if fout != -1 and not fout.closed:
            fout.close()
        fout = open(OUTDIR+"/"+l[1:].strip(),"w")
    else:
        fout.write(l)
fout.close()
f.close()
