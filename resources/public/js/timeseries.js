var $, render, valFormat;

$ = jQuery;

valFormat = function(date) {
  var d, day, month, year, zeropad;
  zeropad = Dygraph.zeropad;
  d = new Date(date);
  year = "" + d.getFullYear();
  month = zeropad(d.getMonth() + 1);
  day = zeropad(d.getDate());
  return year + "/" + month + "/" + day + " " + Dygraph.hmsString_(date);
};

render = function(data) {
  var g;
  $("#title").html(data.title);
  $("#subtitle").html(data.subtitle);
  g = new Dygraph($('#graphdiv')[0], data.csv, {
    title: data.title,
    ylabel: data.yLabel,
    legend: 'always',
    connectSeparatedPoints: true,
    labelsDiv: $('#status')[0],
    Xvalueparser: function(str) {
      return 1.0 * parseInt(str, 10);
    },
    axes: {
      x: {
        valueFormatter: valFormat,
        axisLabelFormatter: Dygraph.dateAxisFormatter,
        ticker: Dygraph.dateTicker
      }
    }
  });
  $('#loader').hide();
  return;
};

$(document).ready(function() {
  var ajx, vars;
  $('#loader').hide();
  vars = void 0;
  ajx = $.ajax({
    type: 'GET',
    url: '/autocomp',
    dataType: 'json',
    success: function(data) {
      return vars = data;
    },
    data: {},
    async: false
  });
  $("input#vari").autocomplete({
    source: vars
  });
  $('#submit').click(function() {
    var ln, lt, lv, v;
    $('#loader').show();
    v = $("#vari").val();
    lt = $("#lat").val();
    ln = $("#lon").val();
    lv = $("#level").val();
    $.getJSON("/ts/" + v + "/" + lt + "/" + ln + "/" + lv, render);
    return;
  });
  return;
});
