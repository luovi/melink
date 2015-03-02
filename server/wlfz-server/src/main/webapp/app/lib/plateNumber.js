(function() {
  define(['backbone'],function(Backbone){
    'use strict';
  var plateCity = {
      C : ["川"],
      E : ["鄂"],
      G : ["赣", "桂", "贵", "甘"],
      H : ["沪", "黑"],
      J : ["京", "津", "冀", "吉", "晋"],
      L : ["辽", "鲁"],
      M : ["蒙", "闽"],
      N : ["宁"],
      Q : ["琼", "青"],
      S : ["苏", "陕"],
      W : ["皖"],
      X : ["湘", "新"],
      Y : ["渝", "豫", "粤", "云"],
      Z : ["浙", "藏"]
    }
  
    function renderPlateSuggest(t, citys, num) {
    var $suggest, self, tmp_suggest;
    self = this;
    tmp_suggest ='<% _.each(citys,function(list){ %>\n    <div class="pull-left mr20"><p>\n    <% _.each(list,function(city,i){ %><a class="word"><% if (num){ %><%= i+1 %><%= city %><% }; %><%= city %></a><% }); %>\n    </p></div>\n<% }); %>';
    tmp_suggest +='<div class="pull-left"><p><a class="word" href="">A</a><a class="word" href="">B</a><a class="word" href="">C</a><a class="word" href="">D</a><a class="word" href="">E</a><a class="word" href="">F</a><a class="word" href="">G</a></p><p><a class="word" href="">H</a><a class="word" href="">I</a><a class="word" href="">J</a><a class="word" href="">K</a><a class="word" href="">L</a><a class="word" href="">M</a><a class="word" href="">N</a></p><p><a class="word" href="">O</a><a class="word" href="">P</a><a class="word" href="">Q</a><a class="word" href="">R</a><a class="word" href="">S</a><a class="word" href="">T</a><a class="word" href="">U</a></p>';
    tmp_suggest +='<p><a class="word" href="">V</a><a class="word" href="">W</a><a class="word" href="">X</a><a class="word" href="">Y</a><a class="word" href="">Z</a></p><p><a class="word" href="">1</a><a class="word" href="">2</a><a class="word" href="">3</a><a class="word" href="">4</a><a class="word" href="">5</a></p><p><a class="word" href="">6</a><a class="word" href="">7</a><a class="word" href="">8</a><a class="word" href="">9</a><a class="word" href="">0</a><a class="word" href="">关闭</a></p></div>';
    $suggest = $(this.template(tmp_suggest, {
      citys: citys,
      num: num
    }));
    $suggest.find('a').on('click', function(event) {
      var text;
      text = $(event.target).text();
      text = text.length === 1 ? text : text[1];
      t.focus();
      return t.val(text);
    });
    return $('.plateNumber-control').html($suggest);
  }

  $("body").delegate(".plate_number_suggest","click",function(event){
      var citys, key, originCitys, p,$target;
      $target = $("#car-plate_number");
      key = $target.val();
      if (key.length > 0) {
        citys = PlateNumber[key.slice(0, 1).toLocaleUpperCase()];
        if (citys && key.length === 1) {
          this.renderPlateSuggest($target, [citys], true);
        }
        if (citys && key.length === 2) {
          city = citys[parseInt(key[1]) - 1];
          if (city) {
            $target.val(city);
          }
        }
         return this.plateNumberAuto($target, key);
      } else {
        p = plateCity;
        originCitys = [[p['J'][0], p['J'][1], p['H'][0], p['Y'][0], p['J'][2], p['Y'][1], p['Y'][3], p['L'][0]], [p['H'][1], p['X'][0], p['W'][0], p['L'][1], p['X'][1], p['S'][0], p['Z'][0], p['G'][0]], [p['E'][0], p['G'][1], p['G'][3], p['J'][4], p['M'][0], p['S'][1], p['J'][3], p['M'][1]], [p['G'][2], p['Y'][2], p['C'][0], p['Q'][1], p['Z'][1], p['Q'][0], p['N'][0]]];
        return renderPlateSuggest($target, originCitys, false);
      }

  }),

  function plateNumberAuto(target, val) {
    var data, plate_number, self, token, _ref;
    self = this;
    if (val === this.be_plate_number) {
      return;
    }
    if ((_ref = this.opts) != null ? _ref.id : void 0) {
      plate_number = /^[\u4E00-\u9FA5]{1}[a-zA-Z]\w{5}$/;
      if (plate_number.test(val)) {
        this.be_plate_number = val;
        data = {
          plate_number: val,
          car_id: self.opts.id
        };
        token = self._current_user().token;
        return $.ajax({
          url: "/api/cars/" + (self._current_user().id) + "/plate_number/verify",
          data: data,
          async: false,
          beforeSend: function(xhr) {
            return xhr.setRequestHeader('Authorization', token);
          },
          success: function(data, status, xhr) {
            var msg, p_data;
            if (!data.result) {
              p_data = {
                plate_number: val
              };
              msg = data.message;
              return $.ajax({
                type: 'GET',
                url: "/api/cars/" + (self._current_user().id) + "/plate_number/switch/id",
                data: p_data,
                async: false,
                beforeSend: function(xhr) {
                  return xhr.setRequestHeader('Authorization', token);
                },
                success: function(data, status, xhr) {
                  target.siblings('span').remove();
                  target.after("<span class=\"special\"> 已有此车牌号，如需编辑请点击 <a href=\"#cars/" + data.item.car_id + "/edit\">" + val + "</a></span>");
                  return $('.btn-confirm').addClass('btn-disabled').attr('disabled', 'true');
                }
              });
            } else {
              target.siblings('span.special').remove();
              return $('.btn-confirm').removeClass('btn-disabled').removeAttr('disabled');
            }
          }
        });
      } else {
        return target.siblings('span.special').remove();
      }
    } else {

    }
  }
  });
}).call(this);
