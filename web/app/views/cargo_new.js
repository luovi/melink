(function() {
  define(['backbone', 'store', 'lib/city_data', 'lib/city_web', 'text!templates/modules/crumb.html', 'text!templates/forms/cargo_add.html'], function(Backbone, Store, City_data, City_web, tmp_crumb, tmp_cargo_add) {
    'use strict';
    var cargoAddView;
    return cargoAddView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      events: {
        'submit form': 'submit'
      },
      submit: _.debounce(function(event) {
        var $target, attr, self;
        self = this;
        this.dirty = false;
        event.preventDefault();
        $target = $(event.currentTarget);
        attr = _.extend(this._arguments($target.serialize()));
        return this.log(attr);
      }, 2000),
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
