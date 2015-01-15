% CSC488 - Compilers

% A programing using all forms of loop building and loop exit constructs

begin

	% Init variables
	integer A <= 1,
	integer B <= 2,

	% Infinite loop using while
	while T = true do put A + A, skip, end, 

	% How does <= know when it is an assignment or when it is a comparison? 
	% An expression can also be a variable? 

	% Finite loop
	while A <= B do put A <= A + 1, skip, end,

	% infinite loop
	loop put A, skip end,

	% Conditional loops
	loop A <= A + 1 end, if A > 100 then exit, end,

	loop A <= A + 1 end, exit when A > 100,

end