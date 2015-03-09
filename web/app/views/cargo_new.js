(function() {
  define(['backbone'], function(Backbone) {
    'use strict';
    var cargoAddView;
    return cargoAddView = Backbone.View.extend({
      initialize: function() {
        this.render();
        return this.log(8);
      },
      render: function() {
        var $container;
        $container = $('<div class="container">aaaa</div>');
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
