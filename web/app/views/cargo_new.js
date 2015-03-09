(function() {
  define(['backbone', 'store', 'modal', 'text!templates/modules/crumb.html', 'text!templates/forms/cargo_add.html'], function(Backbone, Store, Modal, tmp_crumb, tmp_cargo_add) {
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
        var $target, modal, self;
        self = this;
        this.dirty = false;
        event.preventDefault();
        $target = $(event.currentTarget);
        return modal = new Modal({
          title: "确定删除联系人",
          content: "<div ><p>您确定要删除联系人 吗？</p></div>",
          button: [
            {
              value: "确定",
              "class": "btn-small btn-confirm",
              callback: function() {},
              autoremove: true
            }
          ]
        });
      }, 800),
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
