(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['backbone', 'store', 'spin', 'notify', 'modal', 'settings', 'filter', 'validator'], function(Backbone, Store, Spinner, Notify, Modal, settings, filter, validator) {
    'use strict';
    var _route, _view$;
    Backbone.emulateJSON = true;
    /*
    转当前hash下的query为object
    如login?a=1&b=2, @_arguments() == {a:1,b:2}
    */

    Backbone.Router.prototype._arguments = Backbone.View.prototype._arguments = function(query) {
      var key, params, raw_vars, v, val, _i, _len, _ref;
      if (query == null) {
        query = Backbone.history.fragment.split('?')[1];
      }
      if (!query) {
        return;
      }
      params = {};
      raw_vars = query.split("&");
      for (_i = 0, _len = raw_vars.length; _i < _len; _i++) {
        v = raw_vars[_i];
        _ref = v.split("="), key = _ref[0], val = _ref[1];
        val = decodeURIComponent(val.replace(/\+/g, " "));
        if (__indexOf.call(_.keys(params), key) >= 0) {
          if (!_.isArray(params[key])) {
            params[key] = [params[key]];
          }
          params[key].push(val);
        } else {
          params[key] = val;
        }
      }
      return params;
    };
    /* 通过name获得urlquery的参数值
    如a=1&b=2, getArguments('a') == 1
    */

    Backbone.Router.prototype.getArguments = Backbone.View.prototype.getArguments = function(name) {
      var regex, results;
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
      results = regex.exec(Backbone.history.fragment);
      if (results === null) {
        return "";
      } else {
        return decodeURIComponent(results[1].replace(/\+/g, " "));
      }
    };
    /* underscorejs template
    使用方式:
    this.template(tmplStr, {})
    */

    Backbone.Router.prototype.template = Backbone.View.prototype.template = function(tmpl, params) {
      _.extend(params, {
        getArguments: this.getArguments,
        formatDate: filter.formatDate,
        formatCity: filter.formatCity,
        settings: settings
      });
      return _.template(tmpl, params);
    };
    /* 定义_super
    使用方式:
    remove: ->
        // do something...
        @_super('remove')
    */

    Backbone.Model.prototype._super = Backbone.View.prototype._super = Backbone.Router.prototype._super = Backbone.Collection.prototype._super = function(funcName) {
      return this.constructor.__super__[funcName].apply(this, _.rest(arguments));
    };
    /* 方便debug事件
    使用方式:
    var model = new Backbone.Model();
    model.debugEvents();
    model.trigger('change', 'foo', 'bar'); // event "change" with ['foo', 'bar']
    */

    Backbone.Collection.prototype.debugEvents = Backbone.Model.prototype.debugEvents = Backbone.View.prototype.debugEvents = Backbone.Router.prototype.debugEvents = function() {
      return this.on('all', function(eventName) {
        return this.log('event "' + eventName + '" with ', Array.prototype.slice.call(arguments, 1));
      });
    };
    /* 定义log方便打印哪个对象调用的方法
    this.log('test')
    */

    Backbone.Router.prototype.log = Backbone.Model.prototype.log = Backbone.Collection.prototype.log = Backbone.View.prototype.log = function() {
      var error;
      try {
        return console.log.apply(console, ['[' + this.cid + ']'].concat([].splice.call(arguments, 0)));
      } catch (_error) {
        error = _error;
        return "log the error is ... " + error;
      }
    };
    _view$ = Backbone.View.prototype.$;
    Backbone.View.prototype.$ = function(selector) {
      var element;
      element = _view$.apply(this, arguments);
      if (!element.length === 0) {
        if (typeof console !== "undefined" && console !== null) {
          console.error("[Backbone.View] Warning: selector '" + selector + "' do not match any element");
        }
      }
      return element;
    };
    /* 在model里正则匹配所有属性值
    使用方式:
    var model = new Backbone.Model({ first_name: 'Jordan', last_name: 'Aslam' });
    console.log(model.match(/Jo/)); // true
    */

    Backbone.Model.prototype.match = function(test) {
      return _.any(this.attributes, function(attr) {
        if (_.isRegExp(test)) {
          return test.test(attr);
        } else {
          return attr === test;
        }
      });
    };
    /* 在collection中正则匹配出model
    返回列表
    */

    Backbone.Collection.prototype.search = function(test) {
      return this.filter(function(model) {
        return model.match(test);
      });
    };
    /* 在route中实现before和after
    这两个方法已实现跳转后终止后续方法执行
    */

    _route = Backbone.Router.prototype.route;
    Backbone.Router.prototype.before = function() {};
    Backbone.Router.prototype.after = function() {};
    Backbone.Router.prototype.route = function(route, name, callback) {
      var wrapped;
      if (_.isFunction(name)) {
        callback = name;
        name = '';
      }
      if (!callback) {
        callback = this[name];
      }
      wrapped = _.bind(function() {
        if (this.before.apply(this, arguments) === false) {
          return;
        }
        if (callback.apply(this, arguments) === false) {
          return;
        }
        return this.after.apply(this, arguments);
      }, this);
      return _route.call(this, route, name, wrapped);
    };
    /* 获得当前router的路由信息
    返回 route, fragment, params
    */

    Backbone.Router.prototype.current_route = function() {
      var fragment, matched, params, route, routes, self;
      self = this;
      fragment = Backbone.history.fragment;
      routes = _.pairs(this.routes);
      route = null;
      params = null;
      matched = _.find(routes, function(handler) {
        route = _.isRegExp(handler[0]) ? handler[0] : self._routeToRegExp(handler[0]);
        return route.test(fragment);
      });
      if (matched) {
        params = this._extractParameters(route, fragment);
        route = matched[1];
      }
      return {
        route: route,
        fragment: fragment,
        params: params
      };
    };
    /* 跳转hash
    当trigger为真时执行router里的函数
    否则不执行只改hash
    trigger默认为真
    */

    Backbone.Router.prototype.redirect = Backbone.View.prototype.redirect = function(hash, trigger) {
      if (trigger == null) {
        trigger = true;
      }
      Backbone.history.navigate(hash, {
        trigger: trigger
      });
      return this;
    };
    /* hash中加入query值如a=1&b=2
    不执行跳转, 仅方便模板调用请求参数
    */

    Backbone.View.prototype.changeQuery = function(data, traditional) {
      var hash, query, uri;
      hash = Backbone.history.fragment.split('?')[0];
      uri = "#" + hash;
      query = data && !_.isEmpty(data) ? $.param(data, traditional) : void 0;
      if (query) {
        uri = "#" + hash + "?" + query;
      }
      return this.redirect(uri, false);
    };
    /* 设置model的请求数据格式
    原请求格式为 {'a':1,'b':2}
    改为a=1&b=2
    */

    Backbone.Model.prototype.toJSON = function(options) {
      return $.param(this.attributes, true);
    };
    Backbone.Router.prototype._current_user = Backbone.View.prototype._current_user = Backbone.Model.prototype._current_user = Backbone.Collection.prototype._current_user = function() {
      return Store.get('current_user');
    };
    /* 帮助使用到token的请求生成options
    将token加入到ajax头
    如果参数change为true时, 将改变hash中的query, 
    方便模板中使用getArguments获取到参数
    默认change为false
    TODO: 
        1. ajax加载失败的处理
    */

    Backbone.View.prototype.fetchOptions = Backbone.Model.prototype.fetchOptions = Backbone.Collection.prototype.fetchOptions = function(options, change) {
      var current_user, data, error_callback, _ref;
      if (options == null) {
        options = {};
      }
      if (change === true) {
        options.data = _.extend((_ref = typeof this._arguments === "function" ? this._arguments() : void 0) != null ? _ref : {}, options.data);
        if (!_.isEmpty(options.data)) {
          data = _.clone(options.data);
          this.changeQuery(data, options.traditional);
        }
      }
      current_user = typeof this._current_user === "function" ? this._current_user() : void 0;
      if (current_user) {
        _.extend(options, {
          beforeSend: function(xhr) {
            return xhr.setRequestHeader('Authorization', current_user.token);
          }
        });
      }
      error_callback = options.error;
      _.extend(options, {
        cache: false,
        timeout: 10000,
        error: function(xhr, status, errors) {
          var modal;
          if (status.statusText === 'timeout') {
            if (!$('.modal-content').length) {
              return modal = new Modal({
                title: "服务器请求超时",
                content: "<p class=\"modal-content\">请检查网络连接是否通畅，并重试操作!</p>",
                button: [
                  {
                    value: "确定",
                    "class": "btn-small btn-confirm",
                    callback: function() {
                      return $('.spinner').remove();
                    },
                    autoremove: true
                  }
                ]
              });
            }
          } else {
            return typeof error_callback === "function" ? error_callback(xhr, status, errors) : void 0;
          }
        }
      });
      return options;
    };
    /* 让所有view可以方便使用spin
    而且只创建一次，无副作用
    使用方法:
    $target.append(@spinner().el)
    */

    Backbone.View.prototype._spinner = _.once(function() {
      return new Spinner({
        width: 3,
        radius: 10,
        color: '#0079b3'
      }).spin();
    });
    Backbone.View.prototype.spinner = function() {
      var $spin;
      $spin = $(this._spinner().el);
      $spin.css({
        top: $('body').scrollTop() + $(window).height() / 2
      });
      return {
        el: $spin
      };
    };
    Backbone.Router.prototype.notify = Backbone.View.prototype.notify = _.once(function() {
      return new Notify({
        debug: false,
        delay: 3000
      });
    });
    /* 重置get方法,当值为各种不存在时
    替换成新值
    */

    Backbone.Model.prototype.get = function(attr, newValue) {
      var value;
      if (newValue == null) {
        newValue = '';
      }
      value = this.attributes[attr];
      if (_.isNaN(value) || _.isNull(value) || (value === '')) {
        return newValue;
      }
      return value;
    };
    /* 传入Model类和data 返回model实例
    用于json数据格式化成model
    */

    Backbone.Router.prototype.newModel = Backbone.View.prototype.newModel = function(Model, data) {
      var model;
      if (!data) {
        return data;
      }
      model = new Model;
      model.set(data);
      return model;
    };
    /* 验证单个表单项
    返回的是deferred.promise对象
    示例:
    ajaxValid: (event) ->
        $target = $(event.target)
        @model.validateOne($target).then(success,error)
    */

    Backbone.Model.prototype.validateOne = function(target, options) {
      var callback, deferred, errors, key, name, value, _ref;
      deferred = $.Deferred();
      errors = [];
      key = target.attr('name');
      value = target.val();
      this.set(key, value);
      callback = function() {
        if (errors.length > 0) {
          return deferred.reject(errors);
        } else {
          return deferred.resolve();
        }
      };
      name = ((_ref = this.validation) != null ? _ref[key] : void 0) ? this.validation[key] : key;
      $.when(typeof validator[name] === "function" ? validator[name](this, key, options, errors) : void 0).then(callback, callback);
      return deferred.promise();
    };
    /* Backbone内部验证表单函数
    当model.save时默认执行
    返回的是deferred.promise对象
    且isValid()返回的也是promise对象
    */

    Backbone.Model.prototype.validate = function(deferred, attr, options) {
      var callback, errors, queue, self;
      self = this;
      errors = [];
      callback = function() {
        if (errors.length > 0) {
          return deferred.reject(errors);
        } else {
          return deferred.resolve();
        }
      };
      queue = [];
      _.each(_.keys(attr), function(key) {
        var name, _ref;
        name = ((_ref = self.validation) != null ? _ref[key] : void 0) ? self.validation[key] : key;
        return queue.push(typeof validator[name] === "function" ? validator[name](self, key, options, errors) : void 0);
      });
      $.when.apply(null, queue).then(callback, callback);
      return deferred.promise();
    };
    Backbone.View.prototype.hideErrors = function(target) {
      var $wrap;
      if (target) {
        target.removeClass('error');
        $wrap = $(target.data('wrap'), this.$el);
        if ($wrap && $wrap.length > 0) {
          return $wrap.next('p.error').remove();
        } else {
          return target.next('p.error').remove();
        }
      } else {
        this.$('form input.error').removeClass('error');
        return this.$('form p.error').remove();
      }
    };
    Backbone.View.prototype.showErrors = function(errors, target) {
      var self;
      self = this;
      _.each(errors, function(error) {
        var $error, $wrap, input;
        input = self.$('input[name=' + error.name + ']');
        input.addClass('error');
        $wrap = input.data('wrap') ? $(input.data('wrap'), self.$el) : void 0;
        $error = !($wrap && $wrap.length > 0) ? input.next('p.error') : $wrap.next('p.error');
        if ($error.length) {
          return $error.html(error.message);
        } else {
          if ($wrap && $wrap.length > 0) {
            return $wrap.after('<p class="error">' + error.message + '</p>');
          } else {
            return input.after('<p class="error">' + error.message + '</p>');
          }
        }
      });
      if (target) {
        return self.$('input.error:first').focus();
      }
    };
    Backbone.View.prototype.miniPage = function(cols, options) {
      var $page_obj, getData, page, self, setBtn, setClass, tmp;
      if (options == null) {
        options = {};
      }
      if (options.change == null) {
        options.change = true;
      }
      setBtn = function() {
        var aBtn;
        aBtn = $('.btn-min', $page_obj);
        if (cols.has_next) {
          setClass(aBtn.eq(2), 'btn-confirm', 'btn-cancel');
        } else {
          setClass(aBtn.eq(2), 'btn-cancel', 'btn-confirm');
        }
        if (page === 1) {
          setClass(aBtn.eq(0), 'btn-cancel', 'btn-confirm');
          return setClass(aBtn.eq(1), 'btn-cancel', 'btn-confirm');
        } else {
          setClass(aBtn.eq(0), 'btn-confirm', 'btn-cancel');
          return setClass(aBtn.eq(1), 'btn-confirm', 'btn-cancel');
        }
      };
      setClass = function(el, add, remove) {
        return el.addClass(add).removeClass(remove);
      };
      getData = function() {
        return $.when(cols.fetch(self.fetchOptions({
          data: {
            page_no: page
          }
        }, options.change))).then(function() {
          return setBtn();
        });
      };
      self = this;
      page = parseInt(this._arguments().page_no);
      tmp = "<div class=\"page-box J_pagebox\">\n    <input type=\"button\" class=\"btn-confirm btn-min home_page\" value=\"首页\">\n    <input type=\"button\" class=\"btn-confirm btn-min prev_page\" value=\"上一页\">\n    <input type=\"button\" class=\"btn-confirm btn-min next_page\" value=\"下一页\">\n</div>";
      $page_obj = $(self.template(tmp, {}));
      if ($('.J_pagebox').length) {
        $('.J_pagebox').remove();
      }
      $('.reset_list').prepend($page_obj);
      setBtn();
      $('.next_page', $page_obj).on('click', _.debounce(function() {
        if (cols.has_next) {
          page++;
          return getData();
        }
      }, 400, true));
      $('.prev_page', $page_obj).on('click', _.debounce(function() {
        if (page > 1) {
          page--;
          return getData();
        }
      }, 400, true));
      return $('.home_page', $page_obj).on('click', _.debounce(function() {
        if (page > 1) {
          page = 1;
          return getData();
        }
      }, 400, true));
    };
    /* 列表分页
    用法: 在所在列表的view中，@createPage(cols) 即可
    change参数设置changeQuery，默认为true
    page_no也可以options的data中传入，为不调用changeQuery时也能使用分页功能
    */

    Backbone.View.prototype.createPage = function(cols, options) {
      var colsname, self, _ref;
      if (options == null) {
        options = {};
      }
      if ((_ref = $('.pagination')) != null) {
        _ref.remove();
      }
      if (options.change == null) {
        options.change = true;
      }
      self = this;
      if (cols.url.indexOf('cars') > 0) {
        colsname = 'carsPagesize';
      } else if (cols.url.indexOf('cargos') > 0) {
        colsname = 'cargosPagesize';
      } else if (cols.url.indexOf('contacts') > 0) {
        colsname = 'contactsPagesize';
      } else {
        colsname = 'otherPagesize';
      }
      if (cols.length > 0) {
        return require(['text!templates/modules/pagination.html'], function(tmp) {
          var $page_obj, data;
          if (options.data == null) {
            options.data = {};
          }
          data = {
            page: parseInt(options.data.page_no || self.getArguments('page_no') || 1),
            has_next: cols.has_next,
            total: cols.total,
            colsname: colsname,
            Store: Store
          };
          $page_obj = $(self.template(tmp, data));
          $('li:not(.disabled,.active)>a', $page_obj).on('click', function() {
            var pagesize;
            pagesize = $('.text', $page_obj).val() || 20;
            if (cols) {
              return cols.fetch(self.fetchOptions({
                data: {
                  page_no: $(this).data('page'),
                  page_size: pagesize
                }
              }, options.change));
            }
          });
          if (options.target && _.isElement(options.target[0])) {
            options.target.append($page_obj);
          } else {
            self.$el.append($page_obj);
          }
          return $('.btn-confirm', $page_obj).on('click', function() {
            var pagesize;
            pagesize = $('.text', $page_obj).val();
            Store.set(colsname, pagesize);
            if (cols) {
              return cols.fetch(self.fetchOptions({
                data: {
                  page_no: 1,
                  page_size: pagesize
                }
              }, options.change));
            }
          });
        });
      }
    };
    /* 车牌号控件
    用法：在view中 @plateSuggest(options) 即可
        option: object类型 
            {
                target: * 必填项 input选择器 $('input')
                wrap: 弹出el插入的dom。 默认在input父标签后面。
            }
    */

    Backbone.View.prototype.plateSuggest = function(opts) {
      var $el, initialize, originCitys, p, plateNumber, plateSuggest, render, runEvents,
        _this = this;
      $el = "<% _.each(citys,function(list){ %>\n<p>\n    <% _.each(list, function(v){ %>\n        <span><%= v %></span>\n    <% }); %>\n</p>\n<% }); %>";
      plateNumber = p = {
        C: ["川"],
        E: ["鄂"],
        G: ["赣", "桂", "贵", "甘"],
        H: ["沪", "黑"],
        J: ["京", "津", "冀", "吉", "晋"],
        L: ["辽", "鲁"],
        M: ["蒙", "闽"],
        N: ["宁"],
        Q: ["琼", "青"],
        S: ["苏", "陕"],
        W: ["皖"],
        X: ["湘", "新"],
        Y: ["渝", "豫", "粤", "云"],
        Z: ["浙", "藏"]
      };
      originCitys = [[p['J'][0], p['J'][1], p['H'][0], p['Y'][0], p['J'][2], p['Y'][1], p['Y'][3], p['L'][0]], [p['H'][1], p['X'][0], p['W'][0], p['L'][1], p['X'][1], p['S'][0], p['Z'][0], p['G'][0]], [p['E'][0], p['G'][1], p['G'][3], p['J'][4], p['M'][0], p['S'][1], p['J'][3], p['M'][1]], [p['G'][2], p['Y'][2], p['C'][0], p['Q'][1], p['Z'][1], p['Q'][0], p['N'][0]]];
      initialize = function(opts) {
        var wrap;
        if (!_.isObject(opts) || !opts.target) {
          return;
        }
        if (_this.target == null) {
          _this.target = $(opts.target[0]);
        }
        wrap = opts.wrap ? opts.wrap : _this.target.parent();
        _this.suggestWrap = wrap.after("<div class='plate_number_suggest text-warning ml160' ></div>");
        return runEvents();
      };
      runEvents = function() {
        return $(_this.target).on({
          focus: plateSuggest,
          keyup: plateSuggest
        });
      };
      render = function(citys) {
        var input, suggest;
        input = _this.target;
        suggest = $(_.template($el, {
          citys: citys
        }));
        citys = $('.plate_number_suggest');
        citys.html(suggest);
        citys.css('display', 'block');
        return $('span', citys).on('click', function(e) {
          input.val($(e.currentTarget).text());
          citys.css('display', 'none');
          return input.focus();
        });
      };
      plateSuggest = function(e) {
        var key, originCitysValid, plateNumberValid;
        key = _this.target.val().toUpperCase();
        _this.target.val(key);
        originCitysValid = _.contains(_.flatten(originCitys), key);
        plateNumberValid = _.some(plateNumber, function(v, k) {
          return k === key;
        });
        if (key.length === 0) {
          if (!originCitysValid) {
            render(originCitys);
          }
        }
        if (plateNumberValid) {
          return render([plateNumber[key]]);
        }
      };
      return initialize(opts);
    };
    /* tooltips
    用法: 所在views中，渲染页面后执行 @tooltips(options)
    */

    Backbone.View.prototype.tooltips = function(options) {
      var $tipBox, defaults, elements, self, tooltip;
      self = this;
      defaults = {
        length: 5,
        position: 'bottom'
      };
      options = _.extend(defaults, options);
      $tipBox = $('<div>').addClass('tip_box').hide();
      tooltip = {
        add: function(target, style) {
          var tip_text;
          tip_text = target.attr('tip');
          $tipBox.css(style);
          return $tipBox.text(tip_text).appendTo(target).fadeIn(100);
        },
        remove: function() {
          return $tipBox.hide();
        },
        follow: function(event) {
          var target, tip_text;
          target = $(event.target);
          tip_text = target.attr('tip');
          $tipBox.css('left', event.clientX + 10);
          $tipBox.css('top', event.clientY + $(document).scrollTop() + 10);
          $tipBox.css('opacity', 1);
          return $tipBox.text(tip_text).appendTo('body').stop(false, true).show();
        }
      };
      elements = $('.tips');
      if (elements.length > 0) {
        return _.each(elements, function(el) {
          var $this, oldText, tip_left, tip_text, tip_top;
          $this = $(el);
          tip_text = $this.attr('tip');
          oldText = $this.html();
          if (options.length === 'auto') {
            if (!$this.hasClass('tips_text_auto')) {
              if (!$this.hasClass('no_change')) {
                $this.text(_.str.truncate(_.str.clean(tip_text), $this.innerWidth()));
              } else {
                $this.text(_.str.truncate(_.str.clean(oldText), $this.innerWidth()));
              }
            }
          } else {
            if (!$this.hasClass('tips_text_auto')) {
              if (!$this.hasClass('no_change')) {
                $this.text(_.str.truncate(_.str.clean(tip_text), options.length));
              } else {
                $this.text(_.str.truncate(_.str.clean(oldText), options.length));
              }
            }
          }
          if ($this.attr('tip') && $.trim($this.attr('tip')) !== '') {
            if (options.position === 'top') {
              tip_left = $this.position().left;
              tip_top = $this.position().top - 34;
              $this.hover(function() {
                return tooltip.add($this, {
                  top: tip_top,
                  left: tip_left,
                  opacity: 1
                });
              }, tooltip.remove);
              $();
              return $this.bind('focus', tooltip.remove);
            } else if (options.position === 'bottom') {
              tip_left = $this.position().left;
              tip_top = $this.position().top + $this.height() + 15;
              $this.hover(function() {
                return tooltip.add($this, {
                  top: tip_top,
                  left: tip_left,
                  opacity: 1
                });
              }, tooltip.remove);
              return $this.bind('focus', tooltip.remove);
            } else if (options.position === 'follow') {
              $this.hover(function() {
                return $this.bind('mousemove', tooltip.follow);
              }, tooltip.remove);
              return $this.bind('click', function() {
                $this.unbind('mousemove');
                return tooltip.remove();
              });
            }
          }
        });
      }
    };
    /* 获取车辆模型的值
    获取车辆值
    */

    Backbone.Model.prototype.get_car_model_value = Backbone.View.prototype.get_car_model_value = Backbone.Collection.prototype.get_car_model_value = function(models, name) {
      var model, self;
      self = this;
      model = models.find(function(model) {
        return model.get('name') === name;
      });
      if (model) {
        return model.get('value');
      } else {
        return name;
      }
    };
    return Backbone.View.prototype.setNavBtn = function() {
      var data, fn;
      data = Store.get(this.key) || {};
      fn = _.isEmpty(data) ? 'addClass' : 'removeClass';
      return $('.J_oncheckBtn')[fn]('un');
    };
  });

}).call(this);
