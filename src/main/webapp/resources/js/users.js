$(function() {
    $('form').each(function(ind, el) {

        $el = $(el);
        $el.find('input[name=discount]').change((function($el) {
            return function() {
                $el.data('dirty', 'true');
            }
         })($el));
    });

    $('#save').click(function() {
        $('form').each(function(ind, form) {
            $form = $(form);
            if($form.data('dirty')) {
            	$form.data('dirty', false);
                $.post($form.attr('action'), $form.serialize())
                	.done(function() {
                        var $message = $('#message');
                        $message.html('Успішно збережено');
                        $message.hide();
                        $message.fadeIn();
                        setTimeout(function() {
                        	$message.fadeOut();
                        }, 3000);
					});
					
            }
            

        });
    });
});