(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['jquery', 'underscore'], function($, _) {
    'use strict';
    return {
      RegExps: {
        mobile: /^((((13[0-9])|(15[^4,\D])|(18[0-9])|(17[6-8])|(14[57]))\d{8})|((170[059])\d{7}))$/,
        telephone: /^((0[1,2]{1}\d{1}-?\d{8})|(0[3-9]{1}\d{2}-?\d{7,8}))$/,
        username: /^[\u0391-\uFFE5\w]+$/,
        plate_number: /^[\u4E00-\u9FA5]{1}[a-zA-Z]\w{5}$/,
        device: /^\d{7}$/,
        money: /^[1-9](\d+)?$/,
        float_6: /^\d{1,4}(\.\d{1,2})?$/,
        float_8: /^\d{1,6}(\.\d{1,2})?$/,
        id_card_15: /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/,
        id_card_18: /^\d{17}([\dxX])$/,
        illegal_char: /[~!#$%\^&*()=+|\\\[\]{};':",?\/<>]/,
        email: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/,
        int_2: /^([1-9]){1,2}$/,
        trailer_number: /^[\u4E00-\u9FA5]{1}[a-zA-Z]\w{4}\u6302?$/
      },
      Messages: {
        rangelength: function(min, max) {
          return "长度应在" + min + "～" + max + "之间";
        },
        maxlength: function(max) {
          return "最大长度 (" + max + ")";
        },
        range: function(min, max) {
          return "请输入有效数值（" + min + "～" + max + "）";
        },
        required: "这是必填项",
        email: "请输入正确的邮箱",
        device: "回单夹不正确，格式如 0111001",
        equalTo: "两次输入不一致",
        plate_number: "车牌格式不正确，正确格式如 浙A00000",
        trailer_number: "挂车车牌格式不正确，正确格式如 浙A0000",
        remote: "服务器验证失败",
        username: "只能包括中英文字母、数字和下划线",
        mobile: "请填写真实有效的手机号码",
        telephone: "请填写手机号码或固定电话，固话格式如 0571-56865686",
        illegal_char: "不能包含~!#&amp;%^&*()=+|\\[]{};&#39:&quot;,?/&lt;&gt;等字符",
        id_card: "证件号码格式不正确",
        float_6: "请填写有效的数字, 范围1-9999.99",
        float_8: "请填写有效的数字, 范围1-999999.99",
        money: "请输入正确有效的数值（1～999999)",
        invite_user: "邀请方手机号不能与注册手机号相同",
        int_2: "请填写有效地数字, 范围1-99"
      },
      plate_number: function(model, attr, options, errors) {
        var data, error_message;
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (!this.RegExps.plate_number.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.plate_number
          });
        } else if (__indexOf.call(model.needajax, attr) >= 0) {
          error_message = this.Messages.remote;
          data = {
            'plate_number': model.attributes[attr]
          };
          if (model.id) {
            _.extend(data, {
              car_id: model.id
            });
          }
          return $.ajax(_.extend({
            url: "/api/cars/" + (model._current_user().id) + "/plate_number/verify",
            data: data,
            success: function(data, status, xhr) {
              if (!data.result) {
                return errors.unshift({
                  name: attr,
                  message: data.message || error_message
                });
              }
            },
            error: function(xhr, status, message) {
              return errors.unshift({
                name: attr,
                message: error_message
              });
            }
          }, model.fetchOptions()));
        }
      },
      trailer_number: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.trailer_number.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.trailer_number
          });
        }
      },
      locate_number: function(model, attr, options, errors) {
        var cid, data, error_message, url;
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (!this.RegExps.mobile.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.mobile
          });
        } else if (__indexOf.call(model.needajax, attr) >= 0) {
          error_message = this.Messages.remote;
          data = {
            'locate_number': model.attributes[attr]
          };
          if (model.isCarDriver) {
            cid = model.get('cid');
            if (model.id) {
              _.extend(data, {
                driver_id: model.id
              });
            }
            url = "/api/cars/" + (model._current_user().id) + "/" + cid + "/drivers/locate_number/verify";
          } else {
            if (model.id) {
              _.extend(data, {
                car_id: model.id
              });
            }
            url = "/api/cars/" + (model._current_user().id) + "/locate_number/verify";
          }
          return $.ajax(_.extend({
            url: url,
            data: data,
            success: function(data, status, xhr) {
              if (!data.result) {
                return errors.unshift({
                  name: attr,
                  message: data.message || error_message
                });
              }
            },
            error: function(xhr, status, message) {
              return errors.unshift({
                name: attr,
                message: error_message
              });
            }
          }, model.fetchOptions()));
        }
      },
      reg_contact_number: function(model, attr, options, errors) {
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (!this.RegExps.mobile.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.mobile
          });
        } else {
          if (__indexOf.call(model.needajax, attr) >= 0) {
            return $.ajax({
              url: "/api/users/contact/verify",
              data: {
                'contact_number': model.attributes[attr]
              },
              success: function(data, status, xhr) {
                var error_message;
                if (data.result === 0) {
                  error_message = "该手机号已存在";
                  return errors.unshift({
                    name: attr,
                    message: error_message
                  });
                } else if (data.result === -1) {
                  error_message = "此手机号是黑名单号码！";
                  return errors.unshift({
                    name: attr,
                    message: error_message
                  });
                } else if (data.result === -2) {
                  error_message = "不是有效的手机号码，请检查重试";
                  return errors.unshift({
                    name: attr,
                    message: error_message
                  });
                }
              },
              error: function(xhr, status, message) {
                return errors.unshift({
                  name: attr,
                  message: error_message
                });
              }
            });
          }
        }
      },
      for_contact_number: function(model, attr, options, errors) {
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (!this.RegExps.mobile.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.mobile
          });
        } else {
          return $.ajax({
            url: "/api/users/contact/verify",
            data: {
              'contact_number': model.attributes[attr]
            },
            success: function(data, status, xhr) {
              var error_message;
              if (data.result === 1) {
                error_message = "此手机号未被注册，请检查重试";
                return errors.unshift({
                  name: attr,
                  message: error_message
                });
              } else if (data.result === -1) {
                error_message = "此手机号是黑名单号码！";
                return errors.unshift({
                  name: attr,
                  message: error_message
                });
              } else if (data.result === -2) {
                error_message = "不是有效的手机号码，请检查重试";
                return errors.unshift({
                  name: attr,
                  message: error_message
                });
              }
            },
            error: function(xhr, status, message) {
              return errors.unshift({
                name: attr,
                message: error_message
              });
            }
          });
        }
      },
      contact_number: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr] && !this.RegExps.mobile.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.mobile
          });
        }
      },
      telephone: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr] && !(this.RegExps.telephone.test(model.attributes[attr]) || this.RegExps.mobile.test(model.attributes[attr]))) {
          return errors.unshift({
            name: attr,
            message: this.Messages.telephone
          });
        }
      },
      username: function(model, attr, options, errors) {
        var error_message;
        if (options == null) {
          options = {};
        }
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr]) {
          if (!this.RegExps.username.test(model.attributes[attr])) {
            return errors.unshift({
              name: attr,
              message: this.Messages.username
            });
          } else if (model.attributes[attr].length < 2 || model.attributes[attr].length > 20) {
            return errors.unshift({
              name: attr,
              message: this.Messages.rangelength(2, 20)
            });
          } else {
            error_message = this.Messages.remote;
            if (__indexOf.call(model.needajax, attr) >= 0) {
              return $.ajax({
                url: "/api/users/username/verify",
                data: {
                  'username': model.attributes[attr]
                },
                success: function(data, status, xhr) {
                  if (!data.result) {
                    return errors.unshift({
                      name: attr,
                      message: data.message || error_message
                    });
                  }
                },
                error: function(xhr, status, message) {
                  return errors.unshift({
                    name: attr,
                    message: error_message
                  });
                }
              });
            }
          }
        }
      },
      email: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.email.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.email
          });
        }
      },
      device: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.device.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.device
          });
        }
      },
      old_password: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        }
      },
      password: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) < 0) {
          return;
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr].length < 6 || model.attributes[attr].length > 18) {
          return errors.unshift({
            name: attr,
            message: this.Messages.rangelength(6, 18)
          });
        }
      },
      password_again: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr].length < 6 || model.attributes[attr].length > 18) {
          return errors.unshift({
            name: attr,
            message: this.Messages.rangelength(6, 18)
          });
        } else if (model.attributes[attr] !== $('#password').val()) {
          return errors.unshift({
            name: attr,
            message: this.Messages.equalTo
          });
        }
      },
      driver_name: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr] && !this.RegExps.username.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.username
          });
        }
      },
      height: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.float_6.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.float_6
          });
        }
      },
      width: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.float_6.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.float_6
          });
        }
      },
      volume: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.float_6.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.float_6
          });
        }
      },
      length: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.float_6.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.float_6
          });
        }
      },
      tonnage: function(model, attr, options, errors) {
        if (model.attributes[attr] && !this.RegExps.float_6.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.float_6
          });
        }
      },
      id_card: function(model, attr, options, errors) {
        var value;
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr]) {
          value = model.attributes[attr];
          if (value.length === 15) {
            if (!this.RegExps.id_card_15.test(value)) {
              return errors.unshift({
                name: attr,
                message: this.Messages.id_card
              });
            }
          } else if (!this.RegExps.id_card_18.test(value)) {
            return errors.unshift({
              name: attr,
              message: this.Messages.id_card
            });
          }
        }
      },
      name: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        }
      },
      amount: function(model, attr, options, errors) {
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (!this.RegExps.money.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.money
          });
        }
      },
      company: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0) {
          if (!model.attributes[attr]) {
            return errors.unshift({
              name: attr,
              message: this.Messages.required
            });
          } else if (model.attributes[attr].length < 2 || model.attributes[attr].length > 50) {
            return errors.unshift({
              name: attr,
              message: this.Messages.rangelength(2, 50)
            });
          }
        }
      },
      origin: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        }
      },
      short_title: function(model, attr, options, errors) {
        var value;
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr]) {
          value = model.attributes[attr];
          if (value.length > 50) {
            return errors.unshift({
              name: attr,
              message: this.Messages.maxlength(50)
            });
          } else if (this.RegExps.illegal_char.test(value)) {
            return errors.unshift({
              name: attr,
              message: this.Messages.illegal_char
            });
          }
        }
      },
      value_length: function(model, attr, options, errors) {
        var value;
        if (_.isObject(model.valueLength)) {
          return;
        }
        value = model.attributes[attr];
        if (_.has(model.valueLength, attr)) {
          if (model.valueLength.attr.max && value > model.valueLength.attr.max) {
            return errors.unshift({
              name: attr,
              message: this.Messages.maxlength(model.valueLength.attr.max)
            });
          } else if (model.valueLength.min && value < model.valueLength.min) {
            return errors.unshift({
              name: attr,
              message: this.Messages.minlength(model.valueLength.attr.min)
            });
          }
        }
      },
      captcha: function(model, attr, options, errors) {
        var data, error_message;
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (__indexOf.call(model.needajax, attr) >= 0) {
          error_message = '验证码错误';
          if (!model.get('contact_number')) {
            return errors.unshift({
              name: attr,
              message: "请先填写手机号码"
            });
          } else {
            data = {
              captcha: model.attributes[attr],
              contact_number: model.get('contact_number')
            };
            return $.ajax({
              url: "/api/users/captcha/verify",
              data: data,
              success: function(data, status, xhr) {
                if (!data.result) {
                  return errors.unshift({
                    name: attr,
                    message: data.message || error_message
                  });
                }
              },
              error: function(xhr, status, message) {
                return errors.unshift({
                  name: attr,
                  message: error_message
                });
              }
            });
          }
        }
      },
      city_seat: function(model, attr, options, errors) {
        if (!model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        }
      },
      invite_user: function(model, attr, options, errors) {
        if (model.requires == null) {
          model.requires = [];
        }
        if (__indexOf.call(model.requires, attr) >= 0 && !model.attributes[attr]) {
          return errors.unshift({
            name: attr,
            message: this.Messages.required
          });
        } else if (model.attributes[attr] && !this.RegExps.mobile.test(model.attributes[attr])) {
          return errors.unshift({
            name: attr,
            message: this.Messages.mobile
          });
        } else if (model.attributes[attr] === $('#contact_number').val() && model.attributes[attr].length !== 0) {
          return errors.unshift({
            name: attr,
            message: this.Messages.invite_user
          });
        }
      }
    };
    /* 
    'num-of-copies': (model, attr, options, errors) ->
        if not model.attributes[attr] or not @RegExps.int_2.test(model.attributes[attr])
            errors.unshift {name:attr, message:@Messages.int_2}
    */

  });

}).call(this);
