(function() {
  'use strict';
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['backbone', '/component/models/cargo/cargo_orbits.js'], function(Backbone, orbitsModel) {
    var CargoModel;
    return CargoModel = Backbone.Model.extend({
      _status: {
        0: '已取消',
        10: '未成交',
        20: '待成交',
        30: '待成交',
        99: '已成交'
      },
      getStatusName: function(_status) {
        return this._status[_status];
      },
      getFlagIndex: function(flagName) {
        var item;
        item = _.find(this.get('item').locate_methods, function(item) {
          if (__indexOf.call(item, flagName) >= 0) {
            return item;
          }
        });
        return _.indexOf(this.get('item').locate_methods, item);
      },
      parse: function(response) {
        var item, self;
        self = this;
        if (!response.item) {
          response['status_name'] = self.getStatusName(response.status);
        }
        if (response.item) {
          item = response.item;
          item['status_name'] = self.getStatusName(item.status);
          if (item.user_id === this._current_user().id || item.carrier_user_id === this._current_user().id) {
            self.has_right = true;
          }
          if (item.user_id === this._current_user().id) {
            self.is_owner = true;
          }
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
      requires: ["short_title", "city_origin", "city_destination"],
      valueLength: {
        "carrier_contact_person": {
          "max": 20
        }
      }
    });
  });

}).call(this);
