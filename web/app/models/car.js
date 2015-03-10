(function() {
  define(['backbone'], function(Backbone) {
    'use strict';
    var CarModel;
    return CarModel = Backbone.Model.extend({
      parse: function(response) {
        if (response.item) {
          return response.item;
        } else {
          return response;
        }
      },
      initialize: function(attributes, options) {
        if (attributes == null) {
          attributes = {};
        }
        if (options == null) {
          options = {};
        }
        if (options.uid == null) {
          options.uid = this._current_user().id;
        }
        return this.urlRoot = "/api/cars/" + options.uid;
      }
    });
  });

}).call(this);
