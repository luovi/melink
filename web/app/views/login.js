(function() {
  define(['backbone', 'store', 'text!templates/modules/login_banner.html', 'text!templates/forms/login_form.html'], function(Backbone, Store, tmp_login_banner, tmp_login_form) {
    'use strict';
    var LoginView;
    return LoginView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        var $container;
        $container = $('<div class="container"><div class="row"><div class="pull-left login_banner_box mr20"></div><div class="span4"></div></div></div>');
        $('.login_banner_box', $container).append(this.template(tmp_login_banner, {}));
        $('.span4', $container).append(this.template(tmp_login_form, {}));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
