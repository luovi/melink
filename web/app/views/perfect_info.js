(function() {
  define(['backbone', 'store', 'modal', 'text!templates/modules/crumb.html', 'text!templates/modules/sidebar.html', 'text!templates/forms/perfect_info.html'], function(Backbone, Store, Modal, tmp_crumb, tmp_sidebar, tmp_perfect_info) {
    'use strict';
    var PerfectInfoView;
    return PerfectInfoView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      render: function() {
        var $container;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '#',
              name: '用户中心'
            }
          ],
          current: '修改资料'
        })).append($('<div class="row content-box"><div class="span2"></div><div class="span8"></div></div>'));
        $('.row>.span2', $container).append(this.template(tmp_sidebar, {}));
        $('.row>.span8', $container).append(this.template(tmp_perfect_info, {}));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
