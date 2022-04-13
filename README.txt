## Changes
- Made EdgeConvertFileParser abstract.
- Made parseEdgeFile, parseSaveFile, and resolveConnectors abstract.
- Made new classes ParseEdgeFile and ParseSaveFile that extend EdgeConvertFileParser.
- Made constructor that calls the EdgeConvertFileParser constructor to open file in both ParseEdgeFile and ParseSaveFile.
- Added override method of parseEdgeFile and resolveConnectors to ParseEdgeFile class.
- Added override method of parseSaveFile to ParseSaveFile class.
- Changed the class that is called in EdgeConvertGUI based on file extension (Edg or Sav).

## Fixes
- Fixed bug with SQL output where there was an extra  ,  after the last table fields causing an error.
- Added DROP DATABASE IF EXISTS to stop errors stating database already exists.