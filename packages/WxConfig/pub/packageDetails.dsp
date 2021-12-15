<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<html>
  <head>
    <style>
      table, th, td {
        border: 1px solid black;
      }
    </style>
    <title>WxConfigLight: Details for %value packageName%</title>
  </head>
  <body>
    <h1>WxConfigLight: Details for %value packageName%</h1>
    %invoke wx.config.l8.impl.ws:getPackageDetails%
      %ifvar values -isnull%
      The package is not prepared for WxConfigLight. You can change that by creating either of the
      candidate files below.
      %else%
      
      %end%
      <h2>Config file candidates</h2>
      <ol>
      %loop configFileCandidates%
        <li>%value path% <a href="editFile.dsp?file=%value path encode(url)%&packageName=%value /packageName encode(url)%">edit</a>
          %ifvar exists equals('true')%
            (exists)
          %else%
            (Not found)
          %end%
      %endloop%
      </ol>
    %endinvoke%
  </body>
</html>

