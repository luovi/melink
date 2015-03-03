(function() {
  define(['backbone', 'store'], function(Backbone, Store) {
    'use strict';
    var IndexView;
    return IndexView = Backbone.View.extend({
      initialize: function() {},
      render: function() {
        var self;
        self = this;
        return this.subViews != null ? this.subViews : this.subViews = [];
      },
      remove: function() {
        _.invoke(this.subViews, 'remove');
        return this._super('remove');
      }
    });
  });

}).call(this);
