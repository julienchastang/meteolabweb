$ = jQuery

((container) ->
  start = new Date("2009/01/01 01:00").getTime()
  d1 = ([start+(i*1000*3600*24*36.5),i+Math.random()*30+Math.sin(i/20+Math.random()*2)*20+Math.sin(i/10+Math.random())*10] for i in [0..10])

  options =
    xaxis :
      mode : 'time'
      labelsAngle : 45
    selection :
        mode : 'x'
    HtmlText : false
    title : 'Time'

  drawGraph = (d, opts) ->
    console.log d
    o = Flotr._.extend(
      Flotr._.clone(options)
      opts || {})
    Flotr.draw(
      container
      [ d ]
      o)

  v = "Temperature_height_above_ground"
  lt = 40
  ln = -105.27
  lv = 0

  $.getJSON("/flotrtest/" + v + "/" + lt + "/" + ln + "/" + lv, drawGraph)

  ###
  graph = drawGraph(d1)
  ###
  undefined)($('#container')[0])
