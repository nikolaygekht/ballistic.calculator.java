@echo off
doxygen.exe > nul
del dst\*.* /q > nul
%docgen%/bin/docgen prepareproject.xml