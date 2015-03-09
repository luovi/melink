(function() {
  define(['backbone', 'store', 'dropMenu', 'text!templates/pages/cargos.html'], function(Backbone, Store, DropMenu, tmp_cargos) {
    'use strict';
    var IndexView;
    return IndexView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      enableTooltip: function() {
        var elements, self;
        self = this;
        elements = $('.ellipsis');
        return elements.each(function(target) {
          var $this, txt;
          $this = $(this);
          if (self.checkOverflow($this)) {
            txt = $.trim($this.text());
            return $this.addClass("tips").attr("tip", txt);
          }
        });
      },
      render: function() {
        var $container, self, timeout;
        self = this;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_cargos, {}));
        this.$el.html($container);
        this.dropMenu = new DropMenu({
          el: this.$('table'),
          name: 'table',
          target: '.drop-down-link'
        });
        this.enableTooltip();
        return timeout = setTimeout(function() {
          return self.tooltips({
            position: 'follow'
          });
        }, 0);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
