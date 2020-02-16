$.noConflict();

jQuery(document).ready(function ($) {

    "use strict";

    [].slice.call(document.querySelectorAll('select.cs-select')).forEach(function (el) {
        new SelectFx(el);
    });

    tryToShowFinishButton();

    hideSaveButton();

    hideSaveButtonIfNoQuestions();

    jQuery('.selectpicker').selectpicker;

    $('#menuToggle').on('click', function (event) {
        $('body').toggleClass('open');
    });

    $('.search-trigger').on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        $('.search-trigger').parent('.header-left').addClass('open');
    });

    $('.search-close').on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        $('.search-trigger').parent('.header-left').removeClass('open');
    });

    $('.formProcess').on('click', function () {
        var type = $("input[name='formType']:checked").val();
        $('.type').val(type);
    });
    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
    $('#quiz').submit(function (event) {
        event.preventDefault();
        var h4 = $('h4');
        var total = h4.length;
        var json = '{';
        var isValid = true;
        h4.each(function (index) {
            var radioButtonName = $(this).text();
            var radioButtonValue = $("input[name='" + radioButtonName + "']:checked").val();
            if (!isValidRadioValue(radioButtonValue)) {
                $('.message').show();
                event.preventDefault();
                isValid = false;
            }
            json += "'" + radioButtonName + "'" + " : '" + radioButtonValue + "'";
            if (index !== total - 1) {
                json += ','
            }
        });
        $("html, body").animate({scrollTop: 0}, "slow");
        if (!isValid)
            return;
        json += '}';
        $("#userName").css("text-decoration", "line-through");
        var evaluatedId = $('#evaluatedId').val();
        var reviewerId = $('#reviewerId').val();
        var review = {
            reviewerId: reviewerId,
            evaluatedId: evaluatedId,
            quiz: json
        };
        ajaxPost(review);
    });

    $('#evaluationConfiguration').submit(function (event) {
        event.preventDefault();
        var isValid = true;
        var configuration = {};
        $('.weight-input').each(function (index) {
            var inputValue = $(this).val();
            var isValidCurrentElement = true;
            if (inputValue === "" || isNaN(inputValue)) {
                $(this).addClass('is-invalid');
                isValid = false;
                isValidCurrentElement = false;
            }
            if (isValidCurrentElement) {
                $(this).removeClass('is-invalid');
                var id = $(this).get(0).id;
                configuration[id] = inputValue;
            }
        });
        if (isValid) {
            $.ajax({
                url: '/saveConfiguration',
                type: 'POST',
                data: JSON.stringify(configuration),
                contentType: 'application/json',
                dataType: "json",
                success: function (data) {
                    $('#configSuccess').show();
                }
            });
        }
    });
    $('.resetConfiguration').on('click', function (event) {
        event.preventDefault();
        $('.weight-input').each(function (index) {
            $(this).val("");
            if ($(this).hasClass('is-invalid')) {
                $(this).removeClass('is-invalid');
            }
        });
        $('#configSuccess').hide();
    });
    $('.resetExternalLeadScore').on('click', function (event) {
        event.preventDefault();
        var externalLeadScoreElement = $('#externalLeadScore');
        externalLeadScoreElement.val("");
        if (externalLeadScoreElement.hasClass('is-invalid')) {
            externalLeadScoreElement.removeClass('is-invalid')
        }
        $('#externalLeadScoreSuccess').hide();
    });
    $('#externalLeadScoreForm').submit(function (event) {
        event.preventDefault();
        var externalLeadScoreObject = {};
        var isValid = true;
        var externalLeadScoreElement = $('#externalLeadScore');
        var externalLeadScore = externalLeadScoreElement.val();
        if (externalLeadScore === "" || isNaN(externalLeadScore)) {
            externalLeadScoreElement.addClass('is-invalid');
            isValid = false;
        } else {
            if (externalLeadScore < 10 || externalLeadScore > 50) {
                externalLeadScoreElement.addClass('is-invalid');
                isValid = false;
            }
        }
        if (isValid) {
            externalLeadScoreObject = {
                'externalLeadScore': externalLeadScore
            };
            if (externalLeadScoreElement.hasClass('is-invalid')) {
                externalLeadScoreElement.removeClass('is-invalid')
            }
            $.ajax({
                url: "saveExternalLeadScore",
                type: "POST",
                data: JSON.stringify(externalLeadScoreObject),
                contentType: 'application/json',
                dataType: "json",
                success: function (data) {
                    $('#externalLeadScoreSuccess').show();
                }
            })
        }
    });

    function ajaxPost(review) {
        $.ajax({
            url: '/saveForm',
            type: 'POST',
            data: JSON.stringify(review),
            contentType: 'application/json',
            dataType: "json",
            success: function (data) {
                $("li[class='active'] > a > i").removeClass('menu-icon fa fa-question').addClass('menu-icon fa fa-check');
                tryToShowFinishButton();
                $('.message').hide();
            }
        });
    }

    function tryToShowFinishButton() {
        var isValid = true;
        var iTag = $('.menuItem > li > a > i');
        if (iTag.length == 1) {
            $('.noTeamMembers').show();
            $('#saveForm').hide();
            return false;
        }
        iTag.each(function (index) {
            if ($(this).hasClass('menu-icon fa fa-question')) {
                isValid = false;
            }
        });
        if (isValid) {
            $('#finishEvaluation').show();
        }
        return isValid;
    }

    function hideSaveButtonIfNoQuestions() {
        if ($('#quiz > ol > li').length === 0) {
            if ($('.noTeamMembers').is(":hidden")) {
                $('#saveForm').hide();
                $('.noQuestions').show();
            }
        }
    }

    function hideSaveButton() {
        if ($('#invalidUserIdMessage').is(':visible')) {
            $('#saveForm').hide();
        }
    }

    function isValidRadioValue(value) {
        return typeof value !== 'boolean' && value !== '' && (!isNaN(value) && (value >= 1 && value <= 5));
    }
});