(function() {
  'use strict';
  define(['backbone', '/component/models/cargo/cargo_orbits.js'], function(Backbone, orbitsModel) {
    var CargoModel;
    return CargoModel = Backbone.Model.extend({
      initialize: function(opts) {
        var self;
        if (opts == null) {
          opts = {};
        }
        self = this;
        if (opts.uid == null) {
          opts.uid = this._current_user().id;
        }
        return this.urlRoot = "/api/cargos/" + opts.uid;
      }
    });
  });

}).call(this);
