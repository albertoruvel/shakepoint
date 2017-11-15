/**
 * This script must be `resent on all authenticated views to be able to logout from shakepoint
 */
function logout(){
	$.ajax({
		url: '/shakepoint_security_logout', 
		type: 'POST', 
		
	}); 
}