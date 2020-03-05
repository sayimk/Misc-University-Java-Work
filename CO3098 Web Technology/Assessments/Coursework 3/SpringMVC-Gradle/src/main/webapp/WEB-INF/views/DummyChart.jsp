<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['treemap']});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Location', 'Parent'],
          ['Global',    null],
          ['America',   'Global'],
          ['Europe',    'Global'],
          ['Asia',      'Global'],
          ['Australia', 'Global'],
          ['Africa',    'Global'],
          ['Brazil',    'America'],
          ['USA',       'America'],
          ['Mexico',    'America'],
          ['Canada',    'America'],
          ['France',    'Europe'],
          ['Germany',   'Europe'],
          ['Sweden',    'Europe'],
          ['Italy',     'Europe'],
          ['UK',        'Europe'],
          ['China',     'Asia'],
          ['Japan',     'Asia'],
          ['India',     'Asia'],
          ['Laos',      'Asia'],
          ['Mongolia',  'Asia'],
          ['Israel',    'Asia'],
          ['Iran',      'Asia'],
          ['Pakistan',  'Asia'],
          ['Egypt',     'Africa'],
          ['S. Africa', 'Africa'],
          ['Sudan',     'Africa'],
          ['Congo',     'Africa'],
          ['Zaire',     'Africa']
        ]);

        tree = new google.visualization.TreeMap(document.getElementById('chart_div'));

        tree.draw(data, {
          minColor: '#f00',
          midColor: '#ddd',
          maxColor: '#0d0',
          headerHeight: 15,
          fontColor: 'black',
          showScale: true
        });

      }
    </script>
  </head>
  <body>
    <div id="chart_div" style="width: 900px; height: 500px;"></div>
  </body>
</html>