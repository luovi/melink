(function() {
  define(['backbone', 'store', 'text!templates/modules/header.html'], function(Backbone, Store, tmp_header) {
    'use strict';
    var HeaderView;
    return HeaderView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        var user;
        user = Store.get('current_user').username;
        return this.$el.html(this.template(tmp_header, {
          user: user
        }));
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
