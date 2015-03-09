(function() {
  define(['backbone', 'text!templates/errors/404.html'], function(Backbone, template) {
    'use strict';
    var NotFoundView;
    NotFoundView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        return this.$el.html(this.template(template, {
          current_user: ''
        }));
      }
    });
    return NotFoundView;
  });

}).call(this);
