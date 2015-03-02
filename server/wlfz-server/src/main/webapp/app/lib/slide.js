(function() {
  require(['jquery'], function($) {
    return $.fn.autoSlide = function() {
      var cell, cellNum, cellWidth, container, interval, slide;
      container = $('>div', this);
      cell = container.find('div');
      cellWidth = this.width();
      cellNum = cell.length;
      container.css({
        width: cellWidth * cellNum
      });
      slide = function() {
        var current;
        current = $('div:first', container);
        return current.animate({
          'margin-left': -cellWidth
        }, 3000, function() {
          return current.appendTo(container).css({
            'margin-left': ''
          });
        });
      };
      interval = setInterval(slide, 4000);
      return interval;
    };
  });

}).call(this);
