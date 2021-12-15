<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<html>
  <head>
    <style>
      table, th, td {
        border: 1px solid black;
      }
    </style>
    <title>WxConfigLight: Edit file for package %value packageName%</title>
  </head>
  <body>
    <h1>WxConfigLight: Edit file for package %value packageName%</h1>
    %invoke wx.config.l8.impl.ws:editFile%
      <form method="post" action="editFile.dsp">
        <input type="hidden" name="packageName" value="%value /packageName%"></input>
        <input type="hidden" name="file" value="%value /file%"></input>
        <input type="submit" value="Save"></input>
        <textarea name="fileContent" rows="50" cols="80">%value fileContent%</textarea>
        <input type="submit" value="Save"></input>
      </form>
    %endinvoke%
  </body>
</html>
