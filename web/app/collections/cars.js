(function() {
  define(['backbone', '../models/car'], function(Backbone, CarModel) {
    'use strict';
    var Cars;
    return Cars = Backbone.Collection.extend({
      model: CarModel,
      parse: function(response) {
        this.total = response.total;
        this.has_next = response.has_next;
        return response.items;
      },
      initialize: function(models, options) {
        if (models == null) {
          models = [];
        }
        if (options == null) {
          options = {};
        }
        if (options.uid == null) {
          options.uid = this._current_user().id;
        }
        return this.url = "/api/cars/" + options.uid;
      }
    });
  });

}).call(this);
