(function() {
  define(['jquery'], function($) {
    'use strict';
    var Notify;
    return Notify = (function() {
      Notify.prototype.defaults = {
        position: 'top-right',
        speed: 'fast',
        delay: 5000,
        debug: true
      };

      function Notify(options) {
        var self;
        self = this;
        this.settings = $.extend({}, this.defaults, options);
        if (this.cached == null) {
          this.cached = [];
        }
        this.init();
      }

      Notify.prototype.init = function() {
        var self;
        self = this;
        this.queue = $('<div class="notify-queue"></div>');
        this.queue.addClass(this.settings.position);
        this.counter = $('<div class="notify-counter" style=""></div>');
        this.counter.addClass(this.settings.position);
        this.showCounter();
        this.bindCounterEvent();
        if (this.el) {
          this.el.remove();
        }
        this.el = $('<div id="notify" style=""></div>');
        this.tools = $('<div class="notify-tools">清除全部</div>');
        this.el.append(this.counter).append(this.queue);
        return this.el.prependTo('body');
      };

      Notify.prototype.bindToolsEvent = function() {
        var self;
        self = this;
        return this.tools.on('click', function() {
          self.clear();
          return self.tools.remove();
        });
      };

      Notify.prototype.bindCounterEvent = function() {
        var self;
        self = this;
        return this.counter.on('click', function() {
          self.showMessage(self.tools, self.queue);
          self.bindToolsEvent();
          return $.each(self.cached.slice(0, 9), function(i, message) {
            self.showMessage(message);
            return self.bindCloseEvent(message);
          });
        });
      };

      Notify.prototype.bindCloseEvent = function(message) {
        var self;
        self = this;
        return $('.notify-close', message).on('click', function() {
          self.cached.pop(message);
          message.remove();
          return self.showCounter();
        });
      };

      Notify.prototype.showCounter = function() {
        var length;
        length = this.cached.length < 10 ? this.cached.length : '9+';
        this.counter.html(length);
        if (length === 0) {
          return this.counter.hide();
        } else {
          return this.counter.show();
        }
      };

      Notify.prototype.showMessage = function(message, target) {
        var self;
        self = this;
        this.queue.prepend(message);
        message.slideDown(this.settings.speed);
        clearTimeout(message.timeout);
        this.timer(message);
        if (target == null) {
          target = message;
        }
        target.off();
        target.on('mouseenter', function() {
          return clearTimeout(message.timeout);
        });
        return target.on('mouseleave', function() {
          return self.timer(message);
        });
      };

      Notify.prototype.timer = function(message) {
        var self;
        self = this;
        return message.timeout = setTimeout(function() {
          return message.slideUp(self.settings.speed, function() {
            return message.remove();
          });
        }, self.settings.delay);
      };

      Notify.prototype.show = function(msg, status) {
        var message, self;
        self = this;
        message = $("<div class=\"notify border-" + this.settings.position + " notify-" + status + "\">\n    <img src=\"/static/v/v2/images/close.png\" class=\"notify-close\" title=\"Close\">\n    <div class=\"notify-note\">" + msg + "</div>\n</div>");
        this.cached.push(message);
        this.showCounter();
        this.showMessage(message);
        this.bindCloseEvent(message);
        if (this.settings.debug) {
          return typeof console !== "undefined" && console !== null ? console.log(this.cached) : void 0;
        }
      };

      Notify.prototype.clear = function() {
        var self;
        self = this;
        if (this.cached.length > 0) {
          $.each(this.cached, function(i, message) {
            return message.remove();
          });
        }
        this.cached = [];
        return this.showCounter();
      };

      Notify.prototype.remove = function() {
        return this.el.remove();
      };

      Notify.prototype.release = function() {
        this.clear();
        return this.remove();
      };

      return Notify;

    })();
  });

}).call(this);
