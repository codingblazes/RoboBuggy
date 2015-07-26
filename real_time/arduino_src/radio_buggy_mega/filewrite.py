""" filewrite.py: 
* Description: This is a python script to create a .h file defining the time 
*   of compilation and who compiled.  Also details whether files had been
*   modified since the last commit
*
*
*   By Sean Buckley
"""


import os
import datetime
import socket #Used for getting hostname
import subprocess

def main():
    print("Now creating blah.h...\n")
    file = open("comp.h", 'w')
    file.write("/* comp.h: A compilation-time created file containing compilation time and \n")
    file.write("closest version of git*/\n\n")
    time = datetime.datetime.now()
    file.write("#define COMPDATE \"%02d:%02d:%04d\" //day:month:year\n" %(time.day, time.month, time.year))
    file.write("#define COMPTIME \"%02d:%02d:%02d\" //hour:minute:second\n" %(time.hour, time.minute, time.second))
    file.write("#define HOSTNAME \"%s\"\n" %socket.gethostname().split(".")[0])


    result = str(subprocess.check_output(["git", "status"]))

    prev = ""
    branch = ""
    clean = False

    for string in result.split():
        if (prev == "branch"):
            branch = string
        if (string == "clean"):
            clean = True
        prev = string
    
    file.write("#define BRANCHNAME \"%s\"\n" %branch)

    hString = str(subprocess.check_output(["git", "show", "--pretty=format:'%h'", "-n", "1"])).rstrip().replace("'", "")

    file.write("#define COMMITHASH \"%s\"\n" %(hString))
    file.write("#define CLEANSTATUS %s" %str(clean).lower())


    file.close()
    print("File created.\n")

main()