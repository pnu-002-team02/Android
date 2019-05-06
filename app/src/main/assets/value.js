var regions=[
        {
            "region_name": "서울특별시",
            "percentage": 0.2
        },
        {
            "region_name": "부산광역시",
            "percentage": 0.4
        },
        {
            "region_name": "대구광역시",
            "percentage": 0.6
        },
        {
            "region_name": "인천광역시",
            "percentage": 0.8
        },
        {
            "region_name": "광주광역시",
            "percentage": 1
        },
        {
            "region_name": "대전광역시",
            "percentage": 0
        },
        {
            "region_name": "울산광역시",
            "percentage": 0.5
        },
        {
            "region_name": "세종특별자치시",
            "percentage": 0.1
        },
        {
            "region_name": "경기도_수원시",
            "percentage": 0.3
        },
        {
            "region_name": "경기도_성남시",
            "percentage": 0.9
        },
        {
            "region_name": "경기도_의정부시",
            "percentage": 0.9
        },
        {
            "region_name": "경기도_안양시",
            "percentage": 0.9
        }
];

$(function() {
    for(i = 0; i < regions.length; i++) {

        $('#'+ regions[i].region_name)
        .css({'fill': 'rgba(11, 104, 170,' + regions[i].percentage +')'})
        .data('region', regions[i]);
    }

    $('.map g').mouseover(function (e) {
        var region_data=$(this).data('region');
        $('<div class="info_panel">'
            + region_data.region_name
            + '<br>'
            + 'Percentage: '
            + region_data.percentage.toLocaleString("en-UK")
            + '</div>'
         )
        .appendTo('body');
    })
    .mouseleave(function () {
        $('.info_panel').remove();
    })
    .mousemove(function(e) {
        var mouseX = e.pageX,
            mouseY = e.pageY;

        $('.info_panel').css({
            top: mouseY-50,
            left: mouseX - ($('.info_panel').width()/2)
        });
    });

});