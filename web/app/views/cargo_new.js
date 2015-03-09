(function() {
  define(['backbone', 'store', 'text!templates/modules/crumb.html', 'text!templates/forms/cargo_add.html'], function(Backbone, Store, tmp_crumb, tmp_cargo_add) {
    'use strict';
    var cargoAddView;
    return cargoAddView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      events: {
        'submit form': 'submit'
      },
      render: function() {
        var $container, user;
        $container = $('<div class="container"></div>');
        user = Store.get('current_user').username;
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '#',
              name: '货源信息'
            }
          ],
          current: '发布货源'
        }));
        $container.append(this.template(tmp_cargo_add, {
          user: user
        }));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
