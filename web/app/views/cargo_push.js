(function() {
  var __slice = [].slice;

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
          self.addRow($target.val(), $target.data('title'), $target.data('name'));
        } else {
          self.removeRow($target.val());
        }
        this.setCount();
        return this.allIsChecked();
      },
      datchStore: function(event) {
        var $target, self;
        self = this;
        $target = $(event.target);
        if ($target.is(":checked")) {
          $('tbody input:checkbox').attr('checked', 'true');
          $('tbody input:checkbox').each(function(index, item) {
            var target;
            target = $(item);
            return self.addRow(target.val(), target.data('title'), target.data('name'));
          });
        } else {
          $('tbody input:checkbox').removeAttr('checked');
          $('tbody input:checkbox').each(function(index, item) {
            return self.removeRow($(item).val());
          });
        }
        return this.setCount();
      },
      allIsChecked: function() {
        if (this.IsChecked()) {
          return $('thead input:checkbox').attr('checked', 'true');
        } else {
          return $('thead input:checkbox').removeAttr('checked');
        }
      },
      IsChecked: function() {
        var IsChecked;
        IsChecked = true;
        $('tbody input:checkbox').each(function(index, item) {
          if (!$(item).attr('checked')) {
            return IsChecked = false;
          }
        });
        return IsChecked;
      },
      addRow: function() {
        var args, id;
        id = arguments[0], args = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
        this.data = Store.get(this.key) || {};
        if (!this.data[id]) {
          this.data[id] = args;
          this.len++;
        }
        Store.set(this.key, this.data);
        return Store.set(this.key_len, this.len);
      },
      removeRow: function(id) {
        var $target;
        this.data = Store.get(this.key) || {};
        $target = $("i.trash[data-id='" + id + "']", this.$el);
        $target.parents('tr').remove();
        return this["delete"](id);
      },
      "delete": function(id) {
        delete this.data[id];
        this.len--;
        if (this.len < 0) {
          this.len = 0;
        }
        $('#J_selected').html(Store.get(this.key_len));
        Store.set(this.key, this.data);
        return Store.set(this.key_len, this.len);
      },
      rebuildCheckbox: function() {
        var self;
        self = this;
        _.each($('.datch'), function(target) {
          var checked;
          checked = self.inData($(target).val()) ? true : false;
          return $(target).attr('checked', checked);
        });
        this.setCount();
        return this.allIsChecked();
      },
      setCount: function() {
        return $('#J_selected').html(Store.get(this.key_len));
      },
      inData: function(id) {
        return _.has(this.data, id);
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
          return this.carsfilter = new carsFilter(this.options, this.cars);
        }, 0);
      },
      renderList: function() {
        $('.J_car_list').empty().append(this.template(tmp_car_list, {
          cars: this.cars
        }));
        this.createPage(this.cars, {
          target: $('.J_cars_box')
        });
        this.rebuildCheckbox();
        this.side_store = new SideStoreView({
          key: 'mylist',
          target: $('input[type=checkbox]', this.$el)
        });
        this.subViews.push(this.side_store);
        return this.$el.append(this.side_store.el);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
