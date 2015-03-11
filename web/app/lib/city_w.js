(function() {
  define(['jquery', 'underscore', 'lib/city_data'], function($, _, city_data) {
    var CitySelect;
    return CitySelect = (function() {
      CitySelect.prototype.defaults = {
        inputEl: '.city_selet'
      };

      function CitySelect(options) {
        var self;
        if (options == null) {
          options = {};
        }
        self = this;
        this.options = $.extend({}, this.defaults, options);
        this.bindEvent(options);
      }

      CitySelect.prototype.bindEvent = function() {
        var self;
        self = this;
        this.inputEl = this.options.inputEl;
        $('body').delegate(this.inputEl, 'click', function() {
          return self.create($(self.inputEl));
        });
        return $('.J_cityTab').click(function() {
          return console.log(8);
        });
      };

      CitySelect.prototype.create = function(ipt) {
        var common, l, t, tmp_btn, tmp_select;
        tmp_btn = "\n<%if(type === 'J_typePro' ){%>\n	<% $(data).each(function(num,item) { %>\n		<a href=\"javascript:;\" class=\"cb-c-c-btn J_cityBtn J_typePro\"><%= item%></a>\n	<%});%>\n<%}%>\n\n<%if(type === 'J_typeCity' ){%>\n	<% $(data).each(function(num,item) { %>\n		<a href=\"javascript:;\" class=\"cb-c-c-btn J_cityBtn J_typeCity\"><%= item%></a>\n	<%});%>\n<%}%>\n\n<%if(type === 'J_typeCounty' ){%>\n	<% $(data).each(function(num,item) { %>\n		<a href=\"javascript:;\" class=\"cb-c-c-btn J_cityBtn J_typeCounty\"><%= item%></a>\n	<%});%>\n<%}%> \n";
        tmp_select = "<div class=\"citybox\" id=\"J_city_1\" style=\"left:<%=l%>;top:<%=t%>;display:block;\">\n	<input type=\"hidden\" class=\"J_p_hidden J_hidden\">\n	<input type=\"hidden\" class=\"J_n_hidden J_hidden\">\n	<input type=\"hidden\" class=\"J_c_hidden J_hidden\">\n	<div class=\"cb-header\">\n		<div class=\"cb-h-tab\">\n			<a href=\"javascript:;\" class=\"J_cityTab active\">热门</a>\n			<a href=\"javascript:;\" class=\"J_cityTab\">省</a>\n			<a href=\"javascript:;\" class=\"J_cityTab\">市</a>\n			<a href=\"javascript:;\" class=\"J_cityTab\">区县</a>\n		</div>\n		<a href=\"javascript:;\" class=\"cb-close J_cityClose\">×</a>\n	</div>\n	<div class=\"cb-content\">\n		<div class=\"cb-c-cantainer active hot\">\n\n		</div>\n\n		<div class=\"cb-c-cantainer\">\n			\n		</div>\n\n		<div class=\"cb-c-cantainer\">\n		</div>\n\n		<div class=\"cb-c-cantainer\">\n		</div>\n	</div>\n</div>";
        common = this.getList().common;
        this.$btn = $(_.template(tmp_btn, {
          type: 'J_typeCity',
          data: common
        }));
        t = ipt.offset().top + ipt.get(0).offsetHeight + 1 + 'px';
        l = ipt.offset().left + 'px';
        this.$modal = $(_.template(tmp_select, {
          t: t,
          l: l
        }));
        $('.hot', this.$modal).html(this.$btn);
        return $('body').append(this.$modal);
      };

      CitySelect.prototype.getList = function() {
        return {
          common: city_data.commonList,
          cityList: city_data.cityList
        };
      };

      return CitySelect;

    })();
  });

}).call(this);
