(function() {
  define(['backbone', 'store', 'modal', 'text!templates/modules/user_info.html'], function(Backbone, Store, Modal, tmp_user_info) {
    'use strict';
    var centerView;
    return centerView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        var $container;
        $container = $('<div></div>');
        $container.append(this.template(tmp_user_info, {}));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
