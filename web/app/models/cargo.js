(function() {
  'use strict';
  define(['backbone', '/component/models/cargo/cargo_orbits.js'], function(Backbone, orbitsModel) {
    var CargoModel;
    return CargoModel = Backbone.Model.extend({
      validation: {
        "title": "short_title",
        "origin_city": "origin",
        "destin_city": "origin",
        "receiver_name": "value_length",
        "receiver_phone": 'contact_number',
        "sender_phone": 'contact_number',
        "delivery_name": 'short_title'
      },
      parse: function(response) {
        var item, self;
        self = this;
        if (response.item) {
          item = response.item;
        }
        self.set({
          item: item
        });
        if (response.item) {
          return response.item;
        } else {
          return response;
        }
      },
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
      },
      requires: ["login_name", "title", "type"]
    });
  });

}).call(this);
