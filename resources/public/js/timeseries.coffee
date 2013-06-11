$ = jQuery

valFormat = (date) ->
  zeropad = Dygraph.zeropad
  d = new Date(date)
  year = "" + d.getFullYear()
  month = zeropad(d.getMonth() + 1)
  day = zeropad(d.getDate())
  year + "/" + month + "/" + day + " " + Dygraph.hmsString_(date)

render = (data) ->
  $("#title").html(data.title);
  $("#subtitle").html(data.subtitle);
  g = new Dygraph(
    $('#graphdiv')[0]
    data.csv
    title: data.title
    ylabel: data.yLabel
    legend: 'always'
    connectSeparatedPoints: true
    labelsDiv: $('#status')[0]
    Xvalueparser: (str) ->
      1.0 * parseInt(str, 10)
    axes:
      x:
        valueFormatter: valFormat
        axisLabelFormatter: Dygraph.dateAxisFormatter
        ticker: Dygraph.dateTicker)
  $('#loader').hide()
  undefined

$(document).ready ->
  $('#loader').hide()
  vars = undefined
  ajx  =$.ajax(
    type: 'GET'
    url: '/autocomp'
    dataType: 'json'
    success: (data) -> vars = data
    data: {},
    async: false
  )
  $("input#vari").autocomplete(
    source:  vars
  )
  $('#submit').click( () ->
     $('#loader').show()
     v = $("#vari").val()
     lt = $("#lat").val()
     ln = $("#lon").val()
     lv = $("#level").val()
     $.getJSON("/ts/" + v + "/" + lt + "/" + ln + "/" + lv, render)
     undefined
  )
  undefined
