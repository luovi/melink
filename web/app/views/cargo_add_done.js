(function() {
  'use strict';
  define(['backbone', 'models/cargo', 'text!templates/pages/cargo_add_done.html', 'text!templates/modules/crumb.html'], function(Backbone, cargoModel, tmp_done, tmp_crumb) {
    var CargoAddDoneView;
    return CargoAddDoneView = Backbone.View.extend({
      initialize: function(opts) {
        if (opts == null) {
          opts = {};
        }
        return this.render();
      },
      render: function() {
        var $container, self;
        self = this;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: 'cargos',
              name: '货源信息'
            }
          ],
          current: '发布成功'
        }));
        $container.append(this.template(tmp_done, {}));
        return this.$el.html($container);
      }
    });
  });

}).call(this);
