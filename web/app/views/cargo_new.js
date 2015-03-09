(function() {
  define(['backbone', 'text!templates/modules/crumb.html', 'text!templates/forms/cargo_add.html'], function(Backbone, tmp_crumb, tmp_cargo_add) {
    'use strict';
    var cargoAddView;
    return cargoAddView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '#',
              name: '货源信息'
            }
          ],
          current: '发布货源'
        }));
        $container.append(this.template(tmp_cargo_add, {}));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
