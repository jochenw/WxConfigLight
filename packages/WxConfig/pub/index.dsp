<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<html>
  <head>
    <style>
      table, th, td {
        border: 1px solid black;
      }
    </style>
    <title>The WxConfigLight package</title>
  </head>
  <body>
    <h1>The WxConfigLight package</h1>
    %invoke wx.config.l8.impl.ws:getPackageList%
    <h2>General information</h2>
      <dl>
        <dt>Environment:</dt><dd>%value info/environment%</dd>
      </dl>
    <h2>Package list</h2>
    <table>
      <thead>
        <tr><th>Name</th><th>Type</th><th>Details</th></tr>
      </thead>
      <tbody>
        %loop packages%
          <tr>
            <td>%value name%</td>
            <td>
              %ifvar type equals('java')%
              %else%
                %value type%
              %end%
            </td>
            <td>
              %ifvar name equals('Default')%
                Not available
              %else%
                <a target=_blank href="packageDetails.dsp?packageName=%value name encode(url)%">open</a>
              %end%
            </td>
          </tr>
        %endloop%
      </tbody>
    </table>
    %endinvoke%
  </body>
</html>

