(function() {
  define(['backbone', 'store', 'modal', '../collections/cars', 'views/cars_filter', 'views/side_store', 'text!templates/modules/crumb.html', 'text!templates/pages/cargo_push.html', 'text!templates/lists/car_list.html'], function(Backbone, Store, Modal, CarCtrl, carsFilter, SideStoreView, tmp_crumb, tmp_push, tmp_car_list) {
    'use strict';
    var IndexView;
    return IndexView = Backbone.View.extend({
      initialize: function() {
        this.key = 'mylist';
        this.key_len = 'myLen';
        this.len = Store.get(this.key_len) || 0;
        this.data = Store.get(this.key) || {};
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
        'submit form': 'search',
        'click tbody input:checkbox': 'toggleStore',
        'click thead input:checkbox': 'datchStore',
        'click .striped tbody tr': 'toChecked'
      },
      toChecked: function(event) {
        var $checkbox, $target, $tr;
        $target = $(event.target);
        $tr = $target.parents('tr');
        $checkbox = $('.datch', $tr);
        if ($target.hasClass('datch')) {
          return;
        }
        if (!$checkbox.is(":checked")) {
          $checkbox.attr('checked', 'true');
        } else {
          $checkbox.removeAttr('checked');
        }
        return this.toggleStore(false, $checkbox);
      },
      toggleStore: function(event, oTarget) {
        var $target, self;
        self = this;
        $target = event ? $(event.target) : oTarget;
        if ($target.is(":checked")) {
          this.side_store.addRow($target.val(), $target.data('title'), $target.data('name'));
        } else {
          this.side_store.removeRow($target.val());
        }
        return this.allIsChecked();
      },
      datchStore: function(event) {
        var $target, self;
        self = this;
        $target = $(event.target);
        if ($target.is(":checked")) {
          this.$('tbody input:checkbox').attr('checked', 'true');
          return this.$('tbody input:checkbox').each(function(index, item) {
            var target;
            target = $(item);
            return self.side_store.addRow(target.val(), target.data('title'), target.data('name'));
          });
        } else {
          this.$('tbody input:checkbox').removeAttr('checked');
          return this.$('tbody input:checkbox').each(function(index, item) {
            return self.side_store.removeRow($(item).val());
          });
        }
      },
      allIsChecked: function() {
        if (this.IsChecked()) {
          return this.$('thead input:checkbox').attr('checked', 'true');
        } else {
          return this.$('thead input:checkbox').removeAttr('checked');
        }
      },
      IsChecked: function() {
        var IsChecked;
        IsChecked = true;
        this.$('tbody input:checkbox').each(function(index, item) {
          if (!$(item).attr('checked')) {
            return IsChecked = false;
          }
        });
        return IsChecked;
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
          current: '推送货源'
        })).append(this.template(tmp_push, {}));
        this.$el.html($container);
        this.$keyword = this.$('#J_qsearchvalue', this.$el);
        return setTimeout(function() {
          return self.carsfilter = new carsFilter(self.options, self.cars);
        }, 0);
      },
      renderList: function() {
        $('.J_car_list').empty().append(this.template(tmp_car_list, {
          cars: this.cars
        }));
        this.createPage(this.cars, {
          target: $('.J_cars_box')
        });
        this.side_store = new SideStoreView({
          $count: $('#J_selected'),
          key: 'mylist',
          target: $('input[type=checkbox]', this.$el)
        });
        this.subViews.push(this.side_store);
        return this.$el.append(this.side_store.el);
      },
      remove: function() {
        _.invoke(this.subViews, 'remove');
        return this._super('remove');
      }
    });
  });

}).call(this);
