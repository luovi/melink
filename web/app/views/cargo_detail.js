(function() {
  define(['backbone', 'models/cargo', 'text!templates/modules/crumb.html', 'text!templates/pages/cargo_detail.html'], function(Backbone, CargoModel, tmp_crumb, tmp_cargo_detail) {
    'use strict';
    var cargoDetailView;
    return cargoDetailView = Backbone.View.extend({
      initialize: function(opts) {
        var self;
        if (opts == null) {
          opts = {};
        }
        self = this;
        this.cargo = new CargoModel;
        this.cargo.set({
          'id': opts.id
        });
        return $.when(this.cargo.fetch(this.fetchOptions())).then(function() {
          return self.render();
        });
      },
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
          current: '货源详情'
        }));
        $container.append(this.template(tmp_cargo_detail, {
          item: this.cargo
        }));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
