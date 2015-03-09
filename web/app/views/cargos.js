(function() {
  define(['backbone', 'store', 'dropMenu', 'modal', '../collections/cargo', 'text!templates/modules/crumb.html', 'text!templates/pages/cargos.html', 'text!templates/lists/cargos.html'], function(Backbone, Store, DropMenu, Modal, CargosCtrl, tmp_crumb, tmp_cargos, tmp_cargos_list) {
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
        this.cargos = new CargosCtrl;
        this.cargos.fetch(this.fetchOptions(this.options, true));
        this.$el.append(this.spinner().el);
        this.render();
        return this.listenTo(this.cargos, 'sync', this.renderList);
      },
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [],
          current: ''
        })).append(this.template(tmp_cargos, {}));
        return this.$el.html($container);
      },
      renderList: function() {
        $('.J_cargo_list').empty().append(this.template(tmp_cargos_list, {
          cargos: this.cargos
        }));
        this.dropMenu = new DropMenu({
          el: this.$('table'),
          name: 'table',
          target: '.drop-down-link'
        });
        return this.createPage(this.cargos, {
          target: $('.J_cargos_box')
        });
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
