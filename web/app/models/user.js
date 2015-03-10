(function() {
  define(['backbone', 'common'], function(Backbone, Common) {
    'use strict';
    var UserModel;
    return UserModel = Backbone.Model.extend({
      validation: {
        "company-company_name": "company",
        "company-business_number": "telephone",
        "company-email": "email",
        "business_number": "contact_number",
        "contact_number": "reg_contact_number"
      },
      initialize: function(options) {
        if (options == null) {
          options = {};
        }
        if (this._current_user()) {
          if (options.uid == null) {
            options.uid = this._current_user().id;
          }
          return this.urlRoot = "/api/users/" + options.uid;
        } else {
          return this.urlRoot = "/api/users";
        }
      },
      parse: function(response) {
        if (response.item) {
          return response.item;
        } else {
          return response;
        }
      },
      isGuest: function() {
        if (this.get('role') === 190) {
          return true;
        } else {
          return false;
        }
      },
      isPartner: function() {
        var _ref;
        if ((_ref = this.get('role')) === 490 || _ref === 480) {
          return true;
        } else {
          return false;
        }
      },
      isTopPartner: function() {
        if (this.get('role') === 490) {
          return true;
        } else {
          return false;
        }
      },
      isSecPartner: function() {
        if (this.get('role') === 480) {
          return true;
        } else {
          return false;
        }
      },
      isMaster: function() {
        if (this.get('role') === 390) {
          return true;
        } else {
          return false;
        }
      },
      isSub: function() {
        if (this.get('role') === 380) {
          return true;
        } else {
          return false;
        }
      },
      isExpired: function() {
        var deadline;
        deadline = Common.parseDate(this.get('membership_use_deadline'));
        if (deadline === null) {
          return false;
        } else {
          return deadline < new Date();
        }
      },
      requires: ['username', 'password', 'password_again', 'company-company_name', 'contact_number'],
      needajax: ['username', 'contact_number', 'captcha']
    });
  });

}).call(this);
