(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

  define(['jquery'], function($) {
    var DropMenu;
    DropMenu = (function() {
      DropMenu.prototype.defaults = {
        target: '.nav-btn',
        menus: '.drop-menu',
        namespaces: 'dropMenu',
        activeClassName: 'active',
        foot_h: 0,
        tip_h: 8
      };

      DropMenu.prototype.$ = function(selector) {
        return $(selector, this.$el);
      };

      function DropMenu(options) {
        this.hide = __bind(this.hide, this);
        this.show = __bind(this.show, this);
        options = $.extend({}, this.defaults, options);
        this.el = options.el, this.target = options.target, this.menus = options.menus, this.name = options.name, this.namespaces = options.namespaces, this.activeClassName = options.activeClassName, this.foot_h = options.foot_h, this.tip_h = options.tip_h;
        if (!this.el || !this.name) {
          throw new Error('错误，请输入 el, name');
        }
        this.$el = $(this.el);
        this.$target = this.$(this.target);
        this.$menus = this.$(this.menus);
        this.bindEvents();
      }

      DropMenu.prototype.bindEvents = function() {
        var event;
        event = ['click', this.name, this.namespaces].join('.');
        this.$el.on(event, this.target, this.show);
        return $(document).on(event, this.hide);
      };

      DropMenu.prototype.show = function(e) {
        var $this, cy, dh, ey, menu, mh, otherMenus;
        e.stopPropagation();
        if (this.name === 'header') {
          this.$target.not($this = $(e.currentTarget).toggleClass(this.activeClassName)).removeClass(this.activeClassName);
          return this.$menus.not($this.next().toggle()).hide();
        } else {
          menu = $(e.currentTarget).next().toggleClass(this.activeClassName).css({
            'top': 24
          });
          menu.parent('.dropDown_box').css('zIndex', '100');
          $('.arrow-bg', menu).css('top', -14);
          otherMenus = this.$menus.not(menu.toggle()).hide();
          otherMenus.parent('.dropDown_box').css('zIndex', '50');
          ey = e.clientY;
          cy = $(window).height();
          mh = menu[0].offsetHeight;
          dh = this.foot_h + this.tip_h;
          if (mh + dh > cy - ey) {
            menu.addClass('pos-top').css('top', parseInt(menu.css('top')) - (mh + this.tip_h + 20));
            return $('.arrow-bg', menu).css('top', mh - this.tip_h);
          } else {
            return menu.removeClass('pos-top');
          }
        }
      };

      DropMenu.prototype.hide = function() {
        var menu;
        if (this.name === 'header') {
          return this.$menus.hide() && this.$target.removeClass(this.activeClassName);
        } else {
          menu = this.$menus.filter("." + this.activeClassName);
          menu.removeClass(this.activeClassName).hide();
          return menu.parent('.dropDown_box').css('zIndex', '50');
        }
      };

      DropMenu.prototype.release = function() {
        var data, event;
        event = !!this.name ? "." + this.name + "." + this.namespaces : "." + this.namespaces;
        this.$el.off(event);
        $(document).off(event);
        return (data = this.$el.data()) && delete data.dropMenu;
      };

      return DropMenu;

    })();
    return DropMenu;
  });

}).call(this);
