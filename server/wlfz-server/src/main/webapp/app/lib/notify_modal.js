(function() {
  'use strict';
  define(['backbone', '/component/models/track_shorten.js', '/component/models/sms.js', '/component/collections/contacts.js', 'modal', 'text!templates/modules/cargo_notify_modal.html', 'popover'], function(Backbone, shortenModel, smsModel, contModel, Modal, tmp_modal, popover) {
    var CargoNotify, tmp_contacts, tmp_contacts_li;
    tmp_contacts = "<ul class=\"ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all active\" style=\"z-index: 1999; position: relative; display: none; width: 190px;\">\n</ul>";
    tmp_contacts_li = "<% _.each(models, function(model) { %>\n<li class=\"ui-menu-item\" data-number=\"<%= model.get('contact_number') %>\">\n    <a class=\"ui-corner-all\" href=\"javascript:;\">\n        <span class=\"value\"><%= (model.get('company') || {}).company_name || model.get('contact_number') %></span><br>\n        <span class=\"desc\"><%= model.get('contact_person') %></span><br>\n    </a>\n</li>\n<% }); %>";
    return CargoNotify = Backbone.View.extend({
      initialize: function(opts) {
        var ajaxOpts, data, self;
        self = this;
        this.cost = 0;
        this.cid = opts.cid;
        this.cname = opts.cname;
        this.model = opts.model;
        this.user_id = this._current_user().id;
        this.contacts = new contModel;
        this.contacts.fetch(this.fetchOptions());
        this.shorten = new shortenModel;
        this.shorten.set();
        data = {
          user_id: this.user_id
        };
        if (this.model === 'car') {
          data['car_id'] = this.cid;
        } else {
          data['cargo_id'] = this.cid;
        }
        ajaxOpts = {
          data: data,
          processData: true,
          success: function() {
            self.modal = new Modal({
              title: '授权跟踪',
              width: 600,
              content: self.template(tmp_modal, {
                shorten: self.shorten
              }),
              button: [
                {
                  value: "确定",
                  "class": "btn-small btn-confirm",
                  callback: function(data) {
                    data = self._arguments(data);
                    return self.settrack(data);
                  },
                  autoremove: true
                }
              ]
            });
            return self.listenTo(self.contacts, 'sync', self.bindContact(self.modal));
          }
        };
        return this.shorten.fetch(_.extend(ajaxOpts, this.fetchOptions()));
      },
      price: function(modal) {
        var $droplist, $trackTimes, locatePrice, self, smsPrice, __setcost, _length;
        self = this;
        locatePrice = 0.2;
        smsPrice = 0.1;
        $trackTimes = $('#track-times', modal.$modal);
        $droplist = $('.droplist', modal.$modal);
        _length = 0;
        __setcost = function(method) {
          return _.map($droplist, function(input) {
            _length = _.filter($droplist, function(input) {
              return $(input).val();
            }).length;
            if (method === 'plus') {
              self.cost = $trackTimes.val() * locatePrice + smsPrice * _length;
            }
            if (method === 'minus') {
              self.cost -= smsPrice * _length;
            }
            return $('.fee-total .error').text(_.str.toNumber(self.cost, 1));
          });
        };
        $('input.checkbox').on('change', function(e) {
          if ($(e.currentTarget).is(':checked')) {
            return __setcost('plus');
          } else {
            if (_.str.toNumber($trackTimes.val() * locatePrice, 1) !== _.str.toNumber($('.fee-total .error').text(), 1)) {
              return $('.fee-total .error').text(_.str.toNumber($trackTimes.val() * locatePrice, 1));
            } else {
              return __setcost('minus');
            }
          }
        });
        _.map($droplist, function(input) {
          return $(input).change(function() {
            _length = _.filter($droplist, function(input) {
              return $(input).val();
            }).length;
            if ($('input.checkbox').is(':checked')) {
              self.cost = $trackTimes.val() * locatePrice + smsPrice * _length;
              return $('.fee-total .error').text(_.str.toNumber(self.cost, 1));
            }
          });
        });
        return $trackTimes.on('keyup', function() {
          if ($('input.checkbox').is(':checked')) {
            self.cost = $trackTimes.val() * locatePrice + smsPrice * _length;
          } else {
            self.cost = $trackTimes.val() * locatePrice;
          }
          return $('.fee-total .error').text(_.str.toNumber(self.cost, 1));
        });
      },
      settrack: function(data) {
        var msg, self, shorten;
        self = this;
        shorten = new shortenModel;
        if (this.model === 'car') {
          shorten.set({
            'tracking_times': data.tracking_times,
            'car_id': this.cid,
            'user_id': this.user_id
          });
        } else {
          shorten.set({
            'tracking_times': data.tracking_times,
            'cargo_id': this.cid,
            'user_id': this.user_id
          });
        }
        shorten.save({}, this.fetchOptions());
        if (data.enabled_sms) {
          this.sms = new smsModel({
            company: self._current_user().company_name,
            model: self.cname,
            msg_code: 'SMS_TRACKING',
            short_url: self.shorten.get('track_url')
          });
          self.sms.set({
            'numbers': _.compact(data.phones),
            'msg_code': 'SMS_TRACKING'
          });
          return self.sms.save({}, _.extend(self.fetchOptions(), {
            data: $.param(self.sms.attributes, true),
            success: function() {
              var msg;
              msg = "" + self.cname + " 授权跟踪设置成功！";
              return self.notify().show(msg, 'success');
              /*
              if self.model == 'car'
                  self.redirect("#cars")
              else
                  self.redirect("#cargos")
              */

            }
          }));
        } else {
          msg = "" + self.cname + " 授权跟踪设置成功！";
          return self.notify().show(msg, 'success');
          /*
          if self.model == 'car'
              self.redirect("#cars")
          else
              self.redirect("#cargos")
          */

        }
      },
      bindContact: function(modal) {
        var self;
        self = this;
        if (this.$contact_droplist == null) {
          this.$contact_droplist = $(this.template(tmp_contacts, {
            contacts: this.contacts
          }));
        }
        this.$contact_droplist.appendTo('.contacts_list');
        self.price(modal);
        _.each($('.droplist', modal.$modal), function(el) {
          $(el).on('keyup', function() {
            var $this, data, leftc, val;
            $this = $(this);
            leftc = $(el).attr('leftc');
            val = $this.val();
            if (val) {
              data = _.filter(self.contacts.models, function(model) {
                return model.get('contact_number').indexOf(val) === 0;
              });
              return self.loadContact($this, data, leftc);
            } else {
              return self.$contact_droplist.hide('fast');
            }
          });
          return $(document).on('click', function(event) {
            event.stopPropagation();
            return self.$contact_droplist.hide('fast');
          });
        });
        _.each($('i.book', modal.$modal), function(i) {
          return $(i).on('click', function(event) {
            var leftc;
            leftc = $(this).prev().attr('leftc');
            event.stopPropagation();
            return self.loadContact($(this).prev(), self.contacts.models, leftc);
          });
        });
        return self.bindPop(modal);
      },
      bindPop: function(modal) {
        var self;
        self = this;
        return $('.fee-total', modal.$modal).popover({
          trigger: 'hover',
          html: true,
          placement: 'top',
          content: "<div style='width:160px'><p><label>短信费：</label><span>0.1 元 / 条</span></p><p><label>定位费：</label><span>0.2 元 / 次</span></p><p>短信、定位失败不收费.</p><p>优先使用套餐优惠.</p></div>"
        });
      },
      loadContact: function(target, data, lf) {
        var self, setLeft;
        self = this;
        this.$contact_droplist.html(this.template(tmp_contacts_li, {
          models: data
        }));
        $('li', this.$contact_droplist).on('click', function() {
          target.val($(this).data('number')).change();
          return self.$contact_droplist.hide('fast');
        });
        this.$contact_droplist.show();
        setLeft = parseInt(lf);
        return this.$contact_droplist.css({
          top: 2,
          left: setLeft
        });
      },
      removeContact: function() {
        return this.$contact_droplist.detach();
      }
    });
  });

}).call(this);
