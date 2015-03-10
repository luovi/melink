(function() {
  define(['backbone', 'store', 'models/user', 'text!templates/modules/header.html'], function(Backbone, Store, UserModel, tmp_header) {
    'use strict';
    var HeaderView;
    return HeaderView = Backbone.View.extend({
      initialize: function() {
        this.user = new UserModel;
        this.current_user = this.newModel(UserModel, this._current_user());
        return this.render();
      },
      render: function() {
        return this.$el.html(this.template(tmp_header, {
          current_user: this.current_user
        }));
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
