(function() {
  define(['backbone', 'store', 'text!templates/modules/header.html'], function(Backbone, Store, tmp_header) {
    'use strict';
    var HeaderView;
    return HeaderView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        return this.$el.html(this.template(tmp_header, {
          aaa: 0
        }));
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
