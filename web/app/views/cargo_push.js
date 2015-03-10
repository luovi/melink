(function() {
  define(['backbone', 'store', 'modal', '../collections/cars', 'text!templates/modules/crumb.html', 'text!templates/pages/cargo_push.html', 'text!templates/lists/car_list.html'], function(Backbone, Store, Modal, CarCtrl, tmp_crumb, tmp_push, tmp_car_list) {
    'use strict';
    var IndexView;
    return IndexView = Backbone.View.extend({
      initialize: function() {
        if (this.subViews == null) {
          this.subViews = [];
        }
        this.options = {
          data: {
            page_no: 1
          }
        };
        this.cars = new CarCtrl;
        this.cars.fetch(this.fetchOptions(this.options, true));
        this.$el.append(this.spinner().el);
        this.render();
        return this.listenTo(this.cars, 'sync', this.renderList);
      },
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [],
          current: '推送货源'
        })).append(this.template(tmp_push, {}));
        return this.$el.html($container);
      },
      renderList: function() {
        $('.J_car_list').empty().append(this.template(tmp_car_list, {
          cars: this.cars
        }));
        return this.createPage(this.cars, {
          target: $('.J_cars_box')
        });
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
