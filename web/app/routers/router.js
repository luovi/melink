(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['backbone', 'store', 'views/header', 'text!templates/modules/footer.html'], function(Backbone, Store, HeaderView, footer) {
    'use strict';
    var AppRouter;
    return AppRouter = Backbone.Router.extend({
      initialize: function(el) {
        return this.el = el;
      },
      routes: {
        '': 'home',
        'login': 'login',
        'logout': 'logout',
        'signup': 'signup',
        'password/reset/step1': 'pswReset',
        'user/login_by_api': 'loginByApi',
        'cargos': 'cargos',
        'cargos/new': 'cargoAdd',
        'cargo/:id/edit': 'cargoEdit',
        'cargo/:id/detail': 'cargoDetail',
        'cargo/detail': 'cargoDetail',
        'cars': 'cars',
        'cars/new': 'carsNew',
        'cars/:id/edit': 'carsEdit',
        '*path': 'notFound'
      },
      anonymous: ['notFound', 'login', 'signup', 'loginByApi', 'pswReset,cargoAdd'],
      before: function() {
        var _current_user, _ref;
        if (!(_ref = this.current_route().route, __indexOf.call(this.anonymous, _ref) >= 0)) {
          _current_user = this._current_user();
          if (_current_user) {
            if (parseInt(_current_user.age) < (((new Date) / 1000).toFixed() - _current_user.max_age_days * 86400)) {
              Store.remove('current_user');
              this.redirect('login');
              return false;
            }
          } else {
            this.redirect('login');
            return false;
          }
        }
      },
      after: function() {
        return window.addRoute("/#" + Backbone.history.fragment);
      },
      notFound: function(path) {
        var self;
        self = this;
        return require(['views/notfound'], function(NotFoundView) {
          return self.switchView(new NotFoundView);
        });
      },
      loginByApi: function() {
        var self;
        self = this;
        return require(['views/login_by_api'], function(loginByApiView) {
          return self.switchView(new loginByApiView);
        });
      },
      login: function() {
        var self, _current_user;
        self = this;
        _current_user = this._current_user();
        if (_current_user && parseInt(_current_user.age) > (((new Date) / 1000).toFixed() - _current_user.max_age_days * 86400)) {
          self.redirect('');
          return false;
        } else {
          Store.remove('current_user');
        }
        return require(['views/login'], function(LoginView) {
          return self.switchView(new LoginView);
        });
      },
      logout: function() {
        var date;
        if (window.isie6) {
          Store.clear();
        }
        date = new Date();
        date.setTime(date.getTime() - 10000);
        document.cookie = "isLogin" + "=a; expires=" + date.toGMTString() + ";path=/";
        this.redirect('login');
        return false;
      },
      signup: function() {
        var current_user, date, self;
        self = this;
        current_user = new UserModel(this._current_user());
        if (current_user.isGuest()) {
          Store.remove('current_user');
          Store.remove('mylist');
          Store.remove('pv_list');
          Store.remove('cg_list');
          Store.remove('pb_list');
          date = new Date();
          date.setTime(date.getTime() - 10000);
          document.cookie = "isLogin" + "=a; expires=" + date.toGMTString() + ";path=/";
        }
        if (self._current_user() && (!current_user.isGuest())) {
          self.redirect('');
          return false;
        }
        return require(['views/signup'], function(SignupView) {
          self.switchView(new SignupView);
          return document.title = '注册';
        });
      },
      cargoAdd: function() {
        var self;
        self = this;
        return require(['views/cargo_new'], function(cargoAddView) {
          return self.switchView(new cargoAddView);
        });
      },
      home: function() {
        var self;
        self = this;
        return require(['views/index'], function(IndexView) {
          self.switchView(new IndexView);
          return document.title = '首页';
        });
      },
      switchView: function(view) {
        var self, _ref, _ref1;
        self = this;
        if ((_ref = this.view) != null) {
          _ref.remove();
        }
        this.view = view;
        if ((_ref1 = this.header) != null) {
          _ref1.remove();
        }
        this.el.html(view.el);
        this.header = new HeaderView;
        this.el.prepend(this.header.el);
        return this.el.append(this.template(footer, {}));
      },
      hasChange: function() {
        var self;
        self = this;
        return function(event) {
          var dialog, oldLocation, _ref;
          if (this.cancelNavigate) {
            event.stopImmediatePropagation();
            this.cancelNavigate = false;
            return;
          }
          if ((_ref = self.view) != null ? _ref.dirty : void 0) {
            oldLocation = event.originalEvent.oldURL || window.oldURL;
            dialog = confirm("该页面内容未保存，是否离开?");
            if (dialog === false) {
              event.stopImmediatePropagation();
              this.cancelNavigate = true;
              return window.location.href = oldLocation;
            }
          }
        };
      }
    });
  });

}).call(this);
