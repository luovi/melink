(function() {
  define(['backbone', 'store', 'base64', 'common', 'text!templates/modules/crumb.html', 'text!templates/modules/login_banner.html', 'text!templates/forms/login_form.html'], function(Backbone, Store, Base64, Common, tmp_crumb, tmp_login_banner, tmp_login_form) {
    'use strict';
    var LoginModel, LoginView;
    LoginModel = Backbone.Model.extend({
      validate: function(attr, options) {
        var errors;
        errors = [];
        if (!attr.login) {
          errors.push({
            name: 'login',
            message: '用户名不能为空'
          });
        }
        if (!attr.password) {
          errors.push({
            name: 'password',
            message: '密码不能为空'
          });
        }
        return errors.length > 0 ? errors : false;
      }
    });
    return LoginView = Backbone.View.extend({
      initialize: function() {
        this.render();
        this.$login_error = this.$('.login_error');
        this.$input_login = this.$('input#login');
        return this.$input_pass = this.$('input#password');
      },
      events: {
        'submit form': 'submit'
      },
      submit: _.debounce(function(event) {
        var $spinWrap;
        event.preventDefault();
        this.hideErrors();
        this.model = new LoginModel({
          login: this.$input_login.val(),
          password: this.$input_pass.val()
        });
        if (!this.model.isValid()) {
          return this.showErrors(this.model.validationError);
        } else {
          $spinWrap = this.$('#spinner-wrap');
          if ($spinWrap.length === 0) {
            $spinWrap = $('<div id="spinner-wrap"></div>');
            this.$el.append($spinWrap);
          }
          $spinWrap.html(this.spinner().el);
          return this.getToken(this.model.get('login'), this.model.get('password'));
        }
      }, 800),
      render: function() {
        var $container, $content;
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '',
              name: '首页'
            }
          ],
          current: '欢迎登录'
        }));
        $content = $('<div class="row"><div class="pull-left login_banner_box mr48"></div><div class="login_form_box pull-right"></div></div>');
        $('.login_banner_box', $content).append(this.template(tmp_login_banner, {}));
        $('.login_form_box', $content).append(this.template(tmp_login_form, {}));
        $container.append($content);
        return this.$el.html($container);
      },
      showErrors: function(errors) {
        var self;
        self = this;
        errors = errors.reverse();
        return _.each(errors, function(error) {
          var input;
          input = self.$('#' + error.name);
          input.addClass('error').focus();
          return self.$login_error.html('<span class="error">' + error.message + '</span>');
        });
      },
      hideErrors: function() {
        this.$('input').removeClass('error');
        return this.$login_error.empty();
      },
      getToken: function(login, pass) {
        var data, self;
        self = this;
        data = 'Basic ' + Base64.encode(login + ':' + pass);
        return $.ajax({
          type: 'GET',
          url: '/api/token' + '?time=' + (new Date()).getTime(),
          beforeSend: function(xhr) {
            return xhr.setRequestHeader('Authorization', data);
          }
        }).done(function(data, success, xhr) {
          var date, expiresDays, user;
          user = Common.decodeToken(data);
          if (window.isie6) {
            Store.clear();
          }
          Store.set('current_user', user);
          date = new Date();
          expiresDays = $('#remember').is(':checked') ? 14 : 2;
          date.setTime(date.getTime() + expiresDays * 24 * 3600 * 1000);
          document.cookie = "isLogin=1; path=/; expires=" + date.toGMTString();
          return self.redirect('');
        }).fail(function(xhr, error, message) {
          var $spinWrap;
          if (xhr.status === 401) {
            self.showErrors([
              {
                name: 'login',
                message: '用户名或密码错误'
              }
            ]);
          } else {
            self.showErrors([
              {
                name: 'login',
                message: '登录失败'
              }
            ]);
          }
          $spinWrap = self.$('#spinner-wrap');
          return $spinWrap.empty();
        });
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
