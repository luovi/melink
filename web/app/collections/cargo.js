(function() {
  'use strict';
  define(['backbone', '../models/cargo'], function(Backbone, CargoModel) {
    var cargos;
    return cargos = Backbone.Collection.extend({
      model: CargoModel,
      parse: function(response) {
        var self;
        self = this;
        self.total = response.total;
        self.has_next = response.has_next;
        _.each(response.items, function(item) {
          if (item.user_id === self._current_user().id || item.carrier_user_id === self._current_user().id) {
            if (item.has_right == null) {
              item.has_right = true;
            }
          }
          if (item.user_id === self._current_user().id) {
            return item.is_owner = true;
          }
        });
        return response.items;
      },
      initialize: function() {
        var uid;
        uid = this._current_user().id;
        return this.url = "/api/cargos/" + uid;
      }
    });
  });

}).call(this);
