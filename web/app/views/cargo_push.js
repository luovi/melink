(function() {
  define(['backbone', 'store', 'modal', '../collections/cars', 'cars_filter', 'text!templates/modules/crumb.html', 'text!templates/pages/cargo_push.html', 'text!templates/lists/car_list.html'], function(Backbone, Store, Modal, CarCtrl, carsFilter, tmp_crumb, tmp_push, tmp_car_list) {
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
      events: {
        'submit form': 'search'
      },
      search: _.debounce(function(event) {
        var $target;
        $target = $(event.target);
        _.extend(this.options.data, {
          q: this.$keyword.val()
        });
        return this.cars.fetch(this.fetchOptions(this.options, true));
      }, 800, true),
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [],
          current: '推送货源'
        })).append(this.template(tmp_push, {}));
        this.$el.html($container);
        this.$keyword = this.$('#J_qsearchvalue', this.$el);
        console.log(this.carsfilter);
        return this.carsfilter = new carsFilter(this.options, self.cars);
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
