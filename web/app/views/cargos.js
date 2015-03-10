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
        this.render();
        return this.listenTo(this.cargos, 'sync', this.renderList);
      },
      events: {
        'click .nav-tabs li a': 'changeList',
        'submit form': 'search',
        'click .select_tab_btn': 'toggleList'
      },
      toggleList: function(event) {
        return $('.nav-tabs').toggle();
      },
      search: _.debounce(function(event) {
        var $target;
        $target = $(event.target);
        _.extend(this.options.data, {
          q: this.$keyword.val()
        });
        return this.cargos.fetch(this.fetchOptions(this.options, true));
      }, 800, true),
      changeList: _.debounce(function(event) {
        var $target;
        $target = $(event.target);
        _.extend(this.options.data, {
          status: $target.data('status')
        });
        $('.select_tab_btn', $target.parents('.select_tab')).val($target.html());
        $target.parents('.nav-tabs').hide();
        return this.cargos.fetch(this.fetchOptions(this.options, true));
      }, 800, true),
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [],
          current: ''
        })).append(this.template(tmp_cargos, {}));
        this.$el.html($container);
        this.$('.J_cargo_list').append(this.spinner().el);
        this.$keyword = this.$('#J_qsearchvalue', this.$el);
        return $(document).bind("click", function(e) {
          var target;
          target = $(e.target);
          if (target.closest(".select_tab_btn").length === 0) {
            return $('.nav-tabs').hide();
          }
        });
      },
      renderList: function() {
        this.$('.J_cargo_list').empty().html(this.template(tmp_cargos_list, {
          cargos: this.cargos
        }));
        this.dropMenu = new DropMenu({
          el: this.$('.J_cargo_list').children(),
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
